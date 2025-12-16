import asyncio
from db import engine

async def test_connection():
    try:
        async with engine.begin() as conn:
            await conn.run_sync(lambda x: None)
        print("Connexion DB OK.")
    except Exception as e:
        print("Erreur DB :", e)

asyncio.run(test_connection())
