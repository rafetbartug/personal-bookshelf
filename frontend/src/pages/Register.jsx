import { useState } from "react";
import api from "../api/axiosConfig";
import { useNavigate, Link } from "react-router-dom";

export default function Register() {
    const [formData, setFormData] = useState({
        username: "",
        email: "",
        password: ""
    });
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Backend'e Kayıt İsteği
            await api.post("/api/auth/register", formData);
            alert("Kayıt başarılı! Giriş yapabilirsiniz.");
            navigate("/login");
        } catch (err) {
            // Backend'den gelen hatayı (örn: 'username taken') gösterelim
            setError(err.response?.data || "Kayıt başarısız oldu.");
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: "400px" }}>
            <div className="card shadow">
                <div className="card-body">
                    <h2 className="text-center mb-4">Kayıt Ol</h2>
                    {error && <div className="alert alert-danger">{error}</div>}

                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Kullanıcı Adı</label>
                            <input name="username" type="text" className="form-control" onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input name="email" type="email" className="form-control" onChange={handleChange} required />
                        </div>
                        <div className="mb-3">
                            <label className="form-label">Şifre</label>
                            <input name="password" type="password" className="form-control" onChange={handleChange} required />
                        </div>
                        <button type="submit" className="btn btn-success w-100">Kayıt Ol</button>
                    </form>
                    <div className="mt-3 text-center">
                        Zaten üye misin? <Link to="/login">Giriş Yap</Link>
                    </div>
                </div>
            </div>
        </div>
    );
}