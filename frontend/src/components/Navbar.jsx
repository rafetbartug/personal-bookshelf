import { Link, useLocation } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

export default function Navbar() {
    const { user, logout } = useContext(AuthContext);
    const location = useLocation();

    // Link aktif mi kontrolü
    const isActive = (path) => location.pathname === path ? "active" : "";

    return (
        <nav className="navbar navbar-expand-lg navbar-glass fixed-top mb-4">
            <div className="container">
                {/* LOGO MANTIĞI: User varsa /books (Uygulama), yoksa / (Landing) */}
                <Link className="navbar-brand brand-gradient" to={user ? "/books" : "/"}>
                    <span style={{ fontSize: "1.5rem" }}>📚</span>
                    <span>Bookshelf</span>
                </Link>

                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
                    <span className="navbar-toggler-icon" style={{ filter: "invert(1)" }}></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarContent">
                    <ul className="navbar-nav me-auto ms-3 align-items-center">
                        {/* 1. ANA SAYFA / KÜTÜPHANE LİNKİ */}
                        <li className="nav-item">
                            <Link className={`nav-link nav-link-custom ${isActive(user ? "/books" : "/")}`} to={user ? "/books" : "/"}>
                                {user ? "Kütüphane" : "Ana Sayfa"}
                            </Link>
                        </li>

                        {/* 2. UYGULAMA LİNKLERİ (Sadece giriş yapanlar görsün) */}
                        {user && (
                            <>
                                <li className="nav-item">
                                    <Link className={`nav-link nav-link-custom ${isActive("/books")}`} to="/books">Tüm Kitaplar</Link>
                                </li>
                                <li className="nav-item">
                                    <Link className={`nav-link nav-link-custom ${isActive("/myshelf")}`} to="/myshelf">Rafım</Link>
                                </li>
                            </>
                        )}

                        {/* 3. ADMIN LİNKLERİ (Sadece Admin görsün) */}
                        {user && user.role === 'ROLE_ADMIN' && (
                            <>
                                <li className="nav-item">
                                    <Link className={`nav-link nav-link-custom text-info ${isActive("/search-external")}`} to="/search-external">
                                        API Ara 🌍
                                    </Link>
                                </li>
                                <li className="nav-item">
                                    <Link className={`nav-link nav-link-custom text-warning fw-bold d-flex align-items-center gap-1 ${isActive("/admin/dashboard")}`} to="/admin/dashboard">
                                        <span>⚡</span> Yönetim
                                    </Link>
                                </li>
                            </>
                        )}
                    </ul>

                    {/* SAĞ TARAF: KULLANICI / GİRİŞ ALANI */}
                    <div className="d-flex align-items-center gap-3">
                        {user ? (
                            <>
                                <Link to="/profile" className="text-decoration-none">
                                    <div className="user-badge text-white">
                                        <div className="bg-gradient bg-primary rounded-circle d-flex justify-content-center align-items-center"
                                             style={{width: "30px", height: "30px", fontSize: "0.8rem", fontWeight: "bold"}}>
                                            {user.username.charAt(0).toUpperCase()}
                                        </div>
                                        <span className="fw-medium">{user.username}</span>
                                        {user.role === 'ROLE_ADMIN' && <span className="badge bg-warning text-dark ms-2" style={{fontSize: "0.6rem"}}>ADMIN</span>}
                                    </div>
                                </Link>

                                <button onClick={logout} className="logout-btn">
                                    Çıkış
                                </button>
                            </>
                        ) : (
                            // GİRİŞ YAPMAMIŞSA: Giriş ve Kayıt Ol Butonları
                            <div className="d-flex gap-2">
                                <Link to="/login" className="btn btn-outline-light btn-sm px-3 rounded-pill fw-bold" style={{border: "1px solid rgba(255,255,255,0.3)"}}>
                                    Giriş Yap
                                </Link>
                                <Link to="/register" className="btn btn-primary btn-sm px-3 rounded-pill fw-bold"
                                      style={{ background: "linear-gradient(90deg, #667eea, #764ba2)", border: "none" }}>
                                    Kayıt Ol
                                </Link>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
}