import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

export default function LandingPage() {
    const navigate = useNavigate();
    const { user } = useContext(AuthContext);

    const handleCtaClick = () => {
        if (user) {
            navigate("/books");
        } else {
            navigate("/login");
        }
    };

    return (
        <div className="container text-center text-white" style={{ marginTop: "100px" }}>
            {/* HERO SECTION */}
            <div className="py-5">
                <h1 className="display-3 fw-bold mb-4" style={{ textShadow: "0 0 20px rgba(118, 75, 162, 0.8)" }}>
                    Kitaplarınızı <span className="text-warning">Özgürce</span> Yönetin
                </h1>
                <p className="lead mb-5" style={{ fontSize: "1.4rem", opacity: 0.9 }}>
                    Kişisel kütüphanenizi oluşturun, okuduklarınızı puanlayın ve <br />
                    kitap tutkunları topluluğuna katılın.
                </p>

                <div className="d-flex justify-content-center gap-3">
                    <button onClick={handleCtaClick} className="btn btn-primary btn-lg px-5 py-3 shadow-lg fs-5">
                        {user ? "Kütüphaneme Dön 🚀" : "Hemen Başla 🚀"}
                    </button>
                    {!user && (
                        <button onClick={() => navigate("/register")} className="btn btn-outline-light btn-lg px-5 py-3 shadow-lg fs-5">
                            Kayıt Ol ✨
                        </button>
                    )}
                </div>
            </div>

            {/* ÖZELLİKLER KARTLARI (DÜZELTİLDİ: Yazılar artık koyu renk) */}
            <div className="row mt-5 pt-5 g-4">
                <div className="col-md-4">
                    {/* DİKKAT: text-white ve bg-transparent SİLİNDİ */}
                    <div className="card h-100 border-0">
                        <div className="card-body text-dark">
                            <div className="mb-3" style={{ fontSize: "4rem" }}>📚</div>
                            <h3 className="h4 fw-bold">Sınırsız Arşiv</h3>
                            <p className="text-muted">Binlerce kitabı veritabanımızdan bulun veya kendiniz ekleyin.</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card h-100 border-0">
                        <div className="card-body text-dark">
                            <div className="mb-3" style={{ fontSize: "4rem" }}>⭐</div>
                            <h3 className="h4 fw-bold">Puanla & Yorumla</h3>
                            <p className="text-muted">Okuduğunuz kitaplara yıldız verin, düşüncelerinizi paylaşın.</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card h-100 border-0">
                        <div className="card-body text-dark">
                            <div className="mb-3" style={{ fontSize: "4rem" }}>📂</div>
                            <h3 className="h4 fw-bold">Kişisel Raflar</h3>
                            <p className="text-muted">"Okuyacaklarım", "Favorilerim" gibi özel raflar oluşturun.</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* FOOTER */}
            <div className="mt-5 pt-5 pb-4 border-top border-secondary">
                <p className="small opacity-50">&copy; 2025 Personal Bookshelf. Tüm hakları saklıdır.</p>
            </div>
        </div>
    );
}