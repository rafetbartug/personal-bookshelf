import { useState, useContext } from "react";
import { AuthContext } from "../context/AuthContext";
import api from "../api/axiosConfig";
import { useNavigate, Link } from "react-router-dom";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            const response = await api.post("/api/auth/login", {
                usernameOrEmail: username,
                password: password
            });

            login(response.data.token);

            navigate("/");

        } catch (err) {
            setError("Giriş başarısız! Kullanıcı adı veya şifre hatalı.");
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: "400px" }}>
            <div className="card shadow">
                <div className="card-body">
                    <h2 className="text-center mb-4">Giriş Yap</h2>
                    {error && <div className="alert alert-danger">{error}</div>}

                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Kullanıcı Adı</label>
                            <input
                                type="text"
                                className="form-control"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                            />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Şifre</label>
                            <input
                                type="password"
                                className="form-control"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                        </div>
                        <button type="submit" className="btn btn-primary w-100">Giriş Yap</button>
                    </form>
                    <div className="mt-3 text-center">
                        Hesabın yok mu? <Link to="/register">Kayıt Ol</Link>
                    </div>
                </div>
            </div>
        </div>
    );
}