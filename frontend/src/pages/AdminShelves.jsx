import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function AdminShelves() {
    const [shelves, setShelves] = useState([]);

    useEffect(() => {
        fetchShelves();
    }, []);

    const fetchShelves = async () => {
        try {
            const res = await api.get("/api/admin/shelves");
            setShelves(res.data);
        } catch (err) {
            alert("Raflar çekilemedi.");
        }
    };

    return (
        <div className="container mt-4">
            <h2 className="mb-4">📂 Sistemdeki Tüm Raflar</h2>
            <div className="row">
                {shelves.map(shelf => (
                    <div key={shelf.id} className="col-md-4 mb-4">
                        <div className="card shadow-sm h-100 border-info">
                            <div className="card-body text-center">
                                <div className="display-4 mb-3">📁</div>
                                <h5 className="card-title">{shelf.name}</h5> {/* Backend'de isme (Sahibi: X) diye eklemiştik */}
                                <span className={`badge ${shelf.public ? 'bg-success' : 'bg-secondary'}`}>
                                    {shelf.public ? "Herkese Açık 🌍" : "Gizli 🔒"}
                                </span>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}