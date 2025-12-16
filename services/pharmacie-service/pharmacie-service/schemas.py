# schemas.py
from pydantic import BaseModel, ConfigDict
from typing import Optional

class MedicamentSchema(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    
    id: Optional[int] = None
    nom_medicament: str
    quantite: int
    prix: float
