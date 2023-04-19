import json

import stripe
from flask import Blueprint, request

from app.db.queries.shop import get_all_shops
from app.db.mutations.user import add_purchased_items
from app.decorators.api_response import api
from app.internal.constants import STRIPE_API_ENDPOINT_SECRET, STRIPE_API_KEY

stripe.api_key = STRIPE_API_KEY


router = Blueprint("admin", __name__, url_prefix="/admin")


@router.get("/shops")
@api.admin
def view_all_shops():
    return get_all_shops()


@router.post("/-/stripe")
def webhook():
    payload = request.get_data()
    sig_header = request.headers.get("Stripe-Signature", None)
    try:
        event = stripe.Webhook.construct_event(
            payload, sig_header, STRIPE_API_ENDPOINT_SECRET
        )
    except ValueError as e:
        # Invalid payload
        return "Invalid payload", 400
    except stripe.error.SignatureVerificationError as e:
        # Invalid signature
        return "Invalid signature", 400

    # Handle the event
    if event["type"] == "checkout.session.completed":
        try:
            print(event)
            obj = event["data"]["object"]
            m = obj["metadata"]
            print(m)
            u = m["userId"]
            items = json.loads(m["items"])
            prices = [
                stripe.Price.retrieve(x, stripe.api_key, expand=["product"])
                for x in items
            ]
            purchased_ranks = [p["product"]["name"] for p in prices]
            add_purchased_items(u, purchased_ranks)
        except KeyError:
            pass
    elif event["type"] == "charge.failed":
        ...

    return "", 200
