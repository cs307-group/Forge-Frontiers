from ..base import db
import uuid


class Inventory(db.Model):
    __tablename__ = "inventory"

    mc_uuid: uuid.UUID = db.Column(
        db.UUID(as_uuid=False), primary_key=True, unique=True
    )
    inventory_json: str = db.Column(db.Text)

    def __init__(self, mc_uuid=None, inventory_json=None):
        self.mc_uuid = mc_uuid
        self.inventory_json = inventory_json

    @property
    def as_json(self):
        return {
            "mc_uuid": str(self.mc_uuid),
            "inventory_json": self.inventory_json,
        }
