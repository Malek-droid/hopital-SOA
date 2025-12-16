# test_read.py
import asyncio
from db import AsyncSessionLocal
from models import Medicament
from sqlalchemy.future import select

async def test_read():
    async with AsyncSessionLocal() as session:
        result = await session.execute(select(Medicament))
        meds = result.scalars().all()
        for m in meds:
            print(m.id, m.nom_medicament, m.quantite, m.prix)

if __name__ == "__main__":
    asyncio.run(test_read())
