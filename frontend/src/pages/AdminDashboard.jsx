import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";

export default function AdminDashboard() {
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        fetchStats();
    }, []);

    const fetchStats = async () => {
        try {
            const res = await api.get("/api/admin/stats");
            setStats(res.data);
        } catch (err) {
            console.error("İstatistikler çekilemedi", err);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return (
        <div className="d-flex justify-content-center align-items-center" style={{ height: "80vh" }}>
            <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Yükleniyor...</span>
            </div>
        </div>
    );

    if (!stats) return <div className="text-center mt-5 text-danger fw-bold">Veri alınamadı! Admin yetkiniz olmayabilir.</div>;

    // Modern ve Butonlu İstatistik Kartı Bileşeni
    const StatCard = ({ title, value, icon, color, btnText, link }) => (
        <div className="col-md-3 mb-4">
            <div className={`card h-100 border-0 shadow-sm overflow-hidden`}
                 style={{ background: `linear-gradient(135deg, var(--bs-${color}), var(--bs-${color}-rgb) 150%)` }}>
                <div className="card-body text-white p-4 position-relative overflow-hidden">
                    <div className="d-flex justify-content-between align-items-start z-1 position-relative">
                        <div>
                            <h6 className="text-uppercase fw-bold mb-1 opacity-75" style={{ letterSpacing: "1px", fontSize: "0.8rem" }}>{title}</h6>
                            <h2 className="display-5 fw-bold mb-0">{value}</h2>
                        </div>
                        <div className="opacity-50" style={{ fontSize: "3.5rem" }}>{icon}</div>
                    </div>
                    {/* Arkaplan İkonu (Dekoratif) */}
                    <div className="position-absolute opacity-25" style={{ fontSize: "8rem", right: "-20px", bottom: "-20px", transform: "rotate(-15deg)" }}>
                        {icon}
                    </div>
                </div>
                {/* Aksiyon Butonu */}
                <button onClick={() => navigate(link)} className={`btn w-100 rounded-0 fw-bold py-3 text-uppercase text-${color} bg-white bg-opacity-90 border-0 hover-overlay`}>
                    {btnText} <span className="ms-2">→</span>
                </button>
            </div>
        </div>
    );

    return (
        <div className="container py-4">
            <div className="d-flex justify-content-between align-items-center mb-5">
                <div>
                    <h2 className="fw-bold mb-0">⚡ Yönetici Paneli</h2>
                    <p className="text-muted mb-0">Sistemin genel durumu ve hızlı işlemler.</p>
                </div>
                <button className="btn btn-light shadow-sm fw-bold" onClick={fetchStats}>🔄 Yenile</button>
            </div>

            {/* İSTATİSTİK KARTLARI (BUTONLU) */}
            <div className="row g-4 mb-5">
                {/* GÜNCELLENMİŞ LİNKLER */}
                <StatCard title="Toplam Kullanıcı" value={stats.totalUsers} icon="👥" color="primary" btnText="Kullanıcıları Yönet" link="/admin/users" />
                <StatCard title="Arşivdeki Kitap" value={stats.totalBooks} icon="📚" color="success" btnText="Kitap Ekle / Düzenle" link="/books" />
                <StatCard title="Toplam Yorum" value={stats.totalRatings} icon="💬" color="warning" btnText="Yorumları İncele" link="/admin/reviews" />
                <StatCard title="Oluşturulan Raf" value={stats.totalShelves} icon="📂" color="info" btnText="Tüm Rafları Gör" link="/admin/shelves" />
            </div>

            <div className="row g-4">
                {/* AYIN KİTAP KURDU KARTI */}
                <div className="col-md-5">
                    <div className="card border-0 shadow h-100">
                        <div className="card-header bg-dark text-white fw-bold py-3 d-flex align-items-center">
                            <span className="me-2 fs-4">🏆</span> Ayın Kitap Kurdu
                        </div>
                        <div className="card-bodytext-center p-5 d-flex flex-column justify-content-center align-items-center" style={{ background: "linear-gradient(to bottom, #f8f9fa, #fff)" }}>
                            {stats.topUser ? (
                                <>
                                    <div className="position-relative mb-4">
                                        <div className="bg-warning bg-gradient rounded-circle d-flex align-items-center justify-content-center shadow-lg"
                                             style={{width: "100px", height: "100px", fontSize: "40px", border: "4px solid white"}}>
                                            {stats.topUser.charAt(0).toUpperCase()}
                                        </div>
                                        <div className="position-absolute top-0 start-50 translate-middle badge rounded-pill bg-danger p-2 shadow-sm" style={{ fontSize: "1.5rem" }}>
                                            👑
                                        </div>
                                    </div>
                                    <h3 className="fw-bold mb-1">{stats.topUser}</h3>
                                    <p className="text-muted fst-italic mb-4">"En çok yorum yapan efsane üye!"</p>
                                    <button onClick={() => navigate(`/profile/${stats.topUser}`)} className="btn btn-outline-dark btn-sm px-4 rounded-pill fw-bold">
                                        Profili Görüntüle
                                    </button>
                                </>
                            ) : (
                                <div className="text-muted fst-italic">Henüz bir şampiyonumuz yok.</div>
                            )}
                        </div>
                    </div>
                </div>

                {/* SİSTEM DURUMU KARTI */}
                <div className="col-md-7">
                    <div className="card border-0 shadow h-100">
                        <div className="card-header bg-secondary text-white fw-bold py-3 d-flex align-items-center">
                            <span className="me-2 fs-4">📢</span> Sistem Durumu & Sağlık
                        </div>
                        <div className="card-body p-4">
                            <div className="list-group list-group-flush mb-4">
                                <div className="list-group-item d-flex justify-content-between align-items-center py-3 border-0 border-bottom">
                                    <div><span className="me-2">🗄️</span> Veritabanı Bağlantısı</div>
                                    <span className="badge bg-success rounded-pill px-3 py-2">AKTİF ✅</span>
                                </div>
                                <div className="list-group-item d-flex justify-content-between align-items-center py-3 border-0 border-bottom">
                                    <div><span className="me-2">🌍</span> OpenLibrary API</div>
                                    <span className="badge bg-success rounded-pill px-3 py-2">ÇALIŞIYOR ✅</span>
                                </div>
                                <div className="list-group-item d-flex justify-content-between align-items-center py-3 border-0">
                                    <div><span className="me-2">🛡️</span> Güvenlik Modülü (JWT)</div>
                                    <span className="badge bg-success rounded-pill px-3 py-2">DEVREDE ✅</span>
                                </div>
                            </div>

                            <h6 className="fw-bold mb-3">Sistem Yükü (Demo)</h6>
                            <div className="progress mb-4" style={{ height: "20px" }}>
                                <div className="progress-bar bg-info progress-bar-striped progress-bar-animated" role="progressbar" style={{ width: "25%" }}>CPU: 25%</div>
                                <div className="progress-bar bg-warning progress-bar-striped progress-bar-animated" role="progressbar" style={{ width: "15%" }}>RAM: 40%</div>
                            </div>

                            <div className="text-end text-muted small">
                                <span className="me-1">🕒</span> Son Güncelleme: <strong>{new Date().toLocaleTimeString()}</strong>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}