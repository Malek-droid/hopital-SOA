# models.py
from sqlalchemy import Column, Integer, String, Float
from sqlalchemy.orm import declarative_base

Base = declarative_base()

class Medicament(Base):
    __tablename__ = "pharmacie"  # nom exact de la table dans MySQL
    id = Column(Integer, primary_key=True, index=True)
    nom_medicament = Column(String(200), nullable=False)
    quantite = Column(Integer, default=0)
    prix = Column(Float, nullable=False)
