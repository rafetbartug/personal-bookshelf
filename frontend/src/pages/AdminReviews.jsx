import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function AdminReviews() {
    const [reviews, setReviews] = useState([]);

    useEffect(() => {
        fetchReviews();
    }, []);

    const fetchReviews = async () => {
        try {
            const res = await api.get("/api/admin/reviews");
            setReviews(res.data);
        } catch (err) {
            alert("Yorumlar çekilemedi.");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bu yorumu silmek istediğine emin misin?")) return;
        try {
            await api.delete(`/api/admin/reviews/${id}`);
            setReviews(reviews.filter(r => r.id !== id));
        } catch (err) {
            alert("Silinemedi.");
        }
    };

    return (
        <div className="container mt-4">
            <h2 className="mb-4">💬 Tüm Yorumlar ve Değerlendirmeler</h2>
            <div className="row">
                {reviews.map(r => (
                    <div key={r.id} className="col-md-6 mb-3">
                        <div className="card shadow-sm h-100">
                            <div className="card-header d-flex justify-content-between align-items-center bg-light">
                                <strong>👤 {r.username}</strong>
                                <small className="text-muted">{new Date(r.ratedAt).toLocaleDateString()}</small>
                            </div>
                            <div className="card-body">
                                <h6 className="card-title text-primary">📖 {r.bookTitle}</h6>
                                <div className="mb-2">
                                    {[...Array(5)].map((_, i) => (
                                        <span key={i} style={{ color: i < r.score ? "#FFD700" : "#ccc" }}>★</span>
                                    ))}
                                </div>
                                <p className="card-text fst-italic">"{r.comment}"</p>
                                <button onClick={() => handleDelete(r.id)} className="btn btn-outline-danger btn-sm w-100 mt-2">
                                    Yorumu Sil 🚫
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}