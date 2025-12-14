import { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import { AuthContext } from "../context/AuthContext";

export default function BookDetail() {
    const { id } = useParams(); // URL'den ID'yi al
    const navigate = useNavigate();
    const { user } = useContext(AuthContext);

    const [book, setBook] = useState(null);
    const [ratings, setRatings] = useState([]);
    const [summary, setSummary] = useState({ count: 0, avg: 0 });

    // Form State'leri
    const [score, setScore] = useState(0);
    const [comment, setComment] = useState("");
    const [myRating, setMyRating] = useState(null);

    useEffect(() => {
        fetchBookData();
        fetchRatings();
    }, [id]);

    const fetchBookData = async () => {
        try {
            const res = await api.get(`/api/books/${id}`);
            setBook(res.data);
        } catch (err) {
            alert("Kitap bulunamadı!");
            navigate("/books");
        }
    };

    const fetchRatings = async () => {
        try {
            const resList = await api.get(`/api/ratings/book/${id}`);
            setRatings(resList.data);

            const resSum = await api.get(`/api/ratings/book/${id}/summary`);
            setSummary(resSum.data);

            // Eğer kullanıcı giriş yapmışsa kendi puanını da çeksin
            if (user) {
                try {
                    const resMy = await api.get(`/api/ratings/book/${id}/my`);
                    setMyRating(resMy.data);
                    setScore(resMy.data.score);
                    setComment(resMy.data.comment);
                } catch (e) {
                    // Henüz puan vermemiş, sorun yok.
                }
            }
        } catch (err) {
            console.error(err);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (myRating) {
                // Güncelleme
                await api.put(`/api/ratings/book/${id}`, { score, comment });
                alert("Yorumun güncellendi! 🔄");
            } else {
                // Yeni Ekleme
                await api.post("/api/ratings", { bookId: id, score, comment });
                alert("Puanın kaydedildi! ✅");
            }
            fetchRatings(); // Listeyi yenile
        } catch (err) {
            alert("İşlem başarısız: " + (err.response?.data?.message || err.message));
        }
    };

    const handleDelete = async () => {
        if (!window.confirm("Yorumunu silmek istediğine emin misin?")) return;
        try {
            await api.delete(`/api/ratings/book/${id}`);
            setMyRating(null);
            setScore(0);
            setComment("");
            fetchRatings();
            alert("Yorum silindi. 🗑️");
        } catch (err) {
            alert("Silinemedi.");
        }
    };

    // Yıldızları çizdirme fonksiyonu
    const renderStars = (currentScore, interactive = false) => {
        return [...Array(5)].map((_, index) => {
            const starValue = index + 1;
            return (
                <span
                    key={index}
                    style={{
                        cursor: interactive ? "pointer" : "default",
                        color: starValue <= currentScore ? "#FFD700" : "#e4e5e9", // Altın Sarısı veya Gri
                        fontSize: "24px"
                    }}
                    onClick={() => interactive && setScore(starValue)}
                >
                    ★
                </span>
            );
        });
    };

    if (!book) return <div className="text-center mt-5">Yükleniyor...</div>;

    return (
        <div className="container mt-4">
            {/* ÜST KISIM: KİTAP DETAYI */}
            <div className="card mb-4 shadow-sm">
                <div className="row g-0">
                    <div className="col-md-3">
                        <img src={book.coverUrl || "https://via.placeholder.com/300"} className="img-fluid rounded-start w-100" alt={book.title} style={{ height: "300px", objectFit: "cover" }} />
                    </div>
                    <div className="col-md-9">
                        <div className="card-body">
                            <h2 className="card-title">{book.title}</h2>
                            <h5 className="text-muted">{book.authorName}</h5>
                            <p className="card-text mt-3">{book.description}</p>

                            <div className="mt-4">
                                <h5>Genel Puan: {summary.avg?.toFixed(1) || "0.0"} / 5.0</h5>
                                <div>{renderStars(Math.round(summary.avg || 0))} <small className="text-muted">({summary.count} oy)</small></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            {/* ORTA KISIM: PUAN VERME FORMU */}
            {user ? (
                <div className="card mb-4">
                    <div className="card-header bg-light">
                        {myRating ? "Senin Yorumun (Düzenle)" : "Bu Kitabı Puanla"}
                    </div>
                    <div className="card-body">
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <label className="form-label d-block">Puanın:</label>
                                {renderStars(score, true)}
                            </div>
                            <div className="mb-3">
                                <textarea
                                    className="form-control"
                                    rows="3"
                                    placeholder="Bu kitap hakkında ne düşünüyorsun?"
                                    value={comment}
                                    onChange={(e) => setComment(e.target.value)}
                                ></textarea>
                            </div>
                            <button className="btn btn-primary">{myRating ? "Güncelle" : "Gönder"}</button>
                            {myRating && (
                                <button type="button" onClick={handleDelete} className="btn btn-danger ms-2">Sil</button>
                            )}
                        </form>
                    </div>
                </div>
            ) : (
                <div className="alert alert-warning">Yorum yapmak için lütfen giriş yapın.</div>
            )}

            {/* ALT KISIM: DİĞER YORUMLAR */}
            <h4 className="mb-3">💬 Kullanıcı Yorumları</h4>
            {ratings.length === 0 ? (
                <p className="text-muted">Henüz yorum yapılmamış. İlk yorumu sen yap!</p>
            ) : (
                <div className="row">
                    {ratings.map(r => (
                        <div key={r.id} className="col-md-6 mb-3">
                            <div className="card h-100">
                                <div className="card-body">
                                    <div className="d-flex justify-content-between">
                                        <h6 className="card-subtitle mb-2 text-primary">{r.username}</h6>
                                        <div>{renderStars(r.score)}</div>
                                    </div>
                                    <p className="card-text small text-muted mb-1">{new Date(r.ratedAt).toLocaleDateString()}</p>
                                    <p className="card-text">{r.comment}</p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}