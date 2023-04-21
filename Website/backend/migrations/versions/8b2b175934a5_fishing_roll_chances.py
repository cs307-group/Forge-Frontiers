"""fishing roll chances

Revision ID: 8b2b175934a5
Revises: 79e96712d407
Create Date: 2023-04-20 19:33:17.550456

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '8b2b175934a5'
down_revision = '79e96712d407'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('fishing_rarities',
    sa.Column('_id', sa.Integer(), nullable=False),
    sa.Column('rarity', sa.TEXT(), nullable=True),
    sa.Column('chance', sa.Integer(), nullable=True),
    sa.PrimaryKeyConstraint('_id'),
    sa.UniqueConstraint('rarity')
    )
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_table('fishing_rarities')
    # ### end Alembic commands ###