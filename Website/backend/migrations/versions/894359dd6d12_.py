"""empty message

Revision ID: 894359dd6d12
Revises: 
Create Date: 2023-04-15 14:00:47.361418

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '894359dd6d12'
down_revision = None
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('stats', schema=None) as batch_op:
        batch_op.add_column(sa.Column('AscensionLevel', sa.Integer(), nullable=True))
        batch_op.add_column(sa.Column('Tier', sa.Integer(), nullable=True))

    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('stats', schema=None) as batch_op:
        batch_op.drop_column('Tier')
        batch_op.drop_column('AscensionLevel')

    # ### end Alembic commands ###