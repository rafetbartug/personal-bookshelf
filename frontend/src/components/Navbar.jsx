import { Link, useLocation } from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "../context/AuthContext";

export default function Navbar() {
    const { user, logout } = useContext(AuthContext);
    const location = useLocation();

    const isActive = (path) => location.pathname === path ? "active" : "";

    return (
        <nav className="navbar navbar-expand-lg navbar-glass fixed-top">
            <div className="container">
                <Link className="navbar-brand brand-gradient" to={user ? "/books" : "/"}>
                    <span style={{ fontSize: "1.5rem", marginRight: "8px" }}>📚</span>
                    <span className="fw-bold">Bookshelf</span>
                </Link>

                <button
                    className="navbar-toggler"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#navbarContent"
                    aria-controls="navbarContent"
                    aria-expanded="false"
                    aria-label="Toggle navigation"
                >
                    {}
                    <span className="navbar-toggler-icon" style={{ filter: "invert(0.6)" }}></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarContent">
                    <ul className="navbar-nav me-auto ms-3 align-items-center">
                        <li className="nav-item">
                            <Link className={`nav-link nav-link-custom ${isActive(user ? "/books" : "/")}`} to={user ? "/books" : "/"}>
                                {user ? "Kütüphane" : "Ana Sayfa"}
                            </Link>
                        </li>

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

                    <div className="d-flex align-items-center gap-3 mt-3 mt-lg-0">
                        {user ? (
                            <>
                                <Link to="/profile" className="text-decoration-none">
                                    <div className="user-badge text-dark d-flex align-items-center gap-2">
                                        <div className="bg-primary text-white rounded-circle d-flex justify-content-center align-items-center"
                                             style={{width: "32px", height: "32px", fontSize: "0.9rem", fontWeight: "bold"}}>
                                            {user.username.charAt(0).toUpperCase()}
                                        </div>
                                        <span className="fw-medium">{user.username}</span>
                                        {user.role === 'ROLE_ADMIN' && <span className="badge bg-warning text-dark ms-1" style={{fontSize: "0.6rem"}}>ADMIN</span>}
                                    </div>
                                </Link>

                                <button onClick={logout} className="btn btn-outline-danger btn-sm rounded-pill px-3">
                                    Çıkış
                                </button>
                            </>
                        ) : (
                            <div className="d-flex gap-2">
                                <Link to="/login" className="btn btn-outline-secondary btn-sm px-3 rounded-pill fw-bold">
                                    Giriş Yap
                                </Link>
                                <Link to="/register" className="btn btn-primary btn-sm px-3 rounded-pill fw-bold">
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