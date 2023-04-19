import stripe
from flask import Blueprint, request

from app.db.queries.shop import get_all_shops
from app.decorators.api_response import api
from app.internal.constants import STRIPE_API_KEY, STRIPE_API_ENDPOINT_SECRET

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
    print(event)
    # Handle the event
    if event["type"] == "charge.succeeded":
        ...
    elif event["type"] == "charge.failed":
        ...

    return "", 200
