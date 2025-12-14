import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import api from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";

export default function Profile() {
    const { user, logout } = useContext(AuthContext);
    const [myRatings, setMyRatings] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        fetchMyRatings();
    }, []);

    const fetchMyRatings = async () => {
        try {
            const res = await api.get("/api/ratings/my-ratings");
            setMyRatings(res.data);
        } catch (err) {
            console.error("Yorumlar yüklenemedi", err);
        } finally {
            setLoading(false);
        }
    };

    // Tarih formatlama
    const formatDate = (dateString) => {
        return new Date(dateString).toLocaleDateString("tr-TR", {
            year: 'numeric', month: 'long', day: 'numeric'
        });
    };

    // Yıldız çizdirme (küçük boy)
    const renderStars = (score) => {
        return [...Array(5)].map((_, index) => (
            <span key={index} style={{ color: index < score ? "#FFD700" : "#ccc" }}>★</span>
        ));
    };

    if (!user) return null;

    return (
        <div className="container mt-5 mb-5">
            <div className="row">
                {/* SOL TARAFA: KULLANICI KARTI */}
                <div className="col-md-4 mb-4">
                    <div className="card shadow-sm text-center p-4">
                        <div className="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3"
                             style={{width: "100px", height: "100px", fontSize: "40px", fontWeight: "bold"}}>
                            {user.username.charAt(0).toUpperCase()}
                        </div>
                        <h3>{user.username}</h3>
                        <p className="text-muted">{user.email || "E-posta yok"}</p>
                        <span className={`badge ${user.role === 'ROLE_ADMIN' ? 'bg-danger' : 'bg-success'} mb-3`}>
                            {user.role === 'ROLE_ADMIN' ? 'Yönetici 🛠️' : 'Kitap Kurdu 📚'}
                        </span>

                        <div className="d-grid gap-2">
                            <button onClick={() => navigate("/myshelf")} className="btn btn-outline-primary">📂 Raflarım</button>
                            <button onClick={logout} className="btn btn-outline-danger">Çıkış Yap 👋</button>
                        </div>
                    </div>
                </div>

                {/* SAĞ TARAF: SON AKTİVİTELER (YORUMLAR) */}
                <div className="col-md-8">
                    <h4 className="mb-3 border-bottom pb-2">📝 Son Değerlendirmelerim</h4>

                    {loading ? (
                        <p>Yükleniyor...</p>
                    ) : myRatings.length === 0 ? (
                        <div className="alert alert-info">Henüz hiç kitap değerlendirmedin.</div>
                    ) : (
                        <div className="list-group">
                            {myRatings.map((rating) => (
                                <div key={rating.id} className="list-group-item list-group-item-action p-3" aria-current="true">
                                    <div className="d-flex w-100 justify-content-between align-items-center">
                                        <div className="d-flex align-items-center gap-3">
                                            {/* Kitap Resmi (Küçük) */}
                                            <img
                                                src={rating.bookCoverUrl || "https://via.placeholder.com/50"}
                                                alt="cover"
                                                style={{width: "50px", height: "75px", objectFit: "cover", borderRadius: "4px"}}
                                            />
                                            <div>
                                                <h5 className="mb-1"
                                                    style={{cursor: "pointer", color: "#0d6efd"}}
                                                    onClick={() => navigate(`/books/${rating.bookId}`)}>
                                                    {rating.bookTitle}
                                                </h5>
                                                <small className="text-muted">Puanın: {renderStars(rating.score)}</small>
                                            </div>
                                        </div>
                                        <small className="text-muted">{formatDate(rating.ratedAt)}</small>
                                    </div>
                                    <p className="mb-1 mt-2 fst-italic">"{rating.comment}"</p>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}