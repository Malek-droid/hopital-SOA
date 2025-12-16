# test_med.py
import pytest
from httpx import AsyncClient, ASGITransport
import sys
sys.path.insert(0, '..')
from main import app

@pytest.mark.anyio
async def test_crud_medicament():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as client:
        # ---------------- POST ----------------
        response = await client.post("/medicaments", json={
            "nom_medicament": "TestMed",
            "quantite": 50,
            "prix": 9.99
        })
        assert response.status_code == 200
        data = response.json()
        assert "id" in data
        med_id = data["id"]

        # ---------------- GET list ----------------
        response = await client.get("/medicaments")
        assert response.status_code == 200
        meds = response.json()
        assert any(m["id"] == med_id for m in meds)

        # ---------------- GET by id ----------------
        response = await client.get(f"/medicaments/{med_id}")
        assert response.status_code == 200
        assert response.json()["id"] == med_id

        # ---------------- PUT ----------------
        response = await client.put(f"/medicaments/{med_id}", json={
            "nom_medicament": "TestMedUpdated",
            "quantite": 100,
            "prix": 19.99
        })
        assert response.status_code == 200
        assert response.json()["nom_medicament"] == "TestMedUpdated"

        # ---------------- DELETE ----------------
        response = await client.delete(f"/medicaments/{med_id}")
        assert response.status_code == 200
        assert response.json()["detail"] == "Médicament supprimé"

        # ---------------- GET après DELETE ----------------
        response = await client.get(f"/medicaments/{med_id}")
        assert response.status_code == 404
