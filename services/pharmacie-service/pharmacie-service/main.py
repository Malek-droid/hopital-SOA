# main.py
from fastapi import FastAPI, Depends, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import FileResponse
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from typing import List
import os

from db import get_db, engine
from models import Medicament, Base
from schemas import MedicamentSchema

app = FastAPI(title="Service Pharmacie")

@app.on_event("startup")
async def startup():
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

# Enable CORS for frontend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/", tags=["Root"])
async def root():
    # Serve the interface.html file
    html_path = os.path.join(os.path.dirname(__file__), "interface.html")
    if os.path.exists(html_path):
        return FileResponse(html_path, media_type="text/html")
    return {"ok": True, "service": "pharmacie"}

@app.get("/medicaments", response_model=List[MedicamentSchema], tags=["Medicaments"])
async def list_medicaments(db: AsyncSession = Depends(get_db)):
    result = await db.execute(select(Medicament))
    meds = result.scalars().all()
    return meds

@app.post("/medicaments", response_model=MedicamentSchema, tags=["Medicaments"])
async def create_medicament(med: MedicamentSchema, db: AsyncSession = Depends(get_db)):
    obj = Medicament(
        nom_medicament=med.nom_medicament,
        quantite=med.quantite,
        prix=med.prix
    )
    db.add(obj)
    await db.commit()
    await db.refresh(obj)
    return obj

@app.get("/medicaments/{med_id}", response_model=MedicamentSchema, tags=["Medicaments"])
async def get_medicament(med_id: int, db: AsyncSession = Depends(get_db)):
    result = await db.execute(select(Medicament).where(Medicament.id == med_id))
    med = result.scalar_one_or_none()
    if not med:
        raise HTTPException(status_code=404, detail="Médicament non trouvé")
    return med

@app.put("/medicaments/{med_id}", response_model=MedicamentSchema, tags=["Medicaments"])
async def update_medicament(med_id: int, data: MedicamentSchema, db: AsyncSession = Depends(get_db)):
    result = await db.execute(select(Medicament).where(Medicament.id == med_id))
    med = result.scalar_one_or_none()
    if not med:
        raise HTTPException(status_code=404, detail="Médicament non trouvé")
    med.nom_medicament = data.nom_medicament
    med.quantite = data.quantite
    med.prix = data.prix
    await db.commit()
    await db.refresh(med)
    return med

@app.delete("/medicaments/{med_id}", tags=["Medicaments"])
async def delete_medicament(med_id: int, db: AsyncSession = Depends(get_db)):
    result = await db.execute(select(Medicament).where(Medicament.id == med_id))
    med = result.scalar_one_or_none()
    if not med:
        raise HTTPException(status_code=404, detail="Médicament non trouvé")
    await db.delete(med)
    await db.commit()
    return {"detail": "Médicament supprimé"}
