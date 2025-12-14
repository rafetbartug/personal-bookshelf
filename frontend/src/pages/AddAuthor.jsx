import { useState } from "react";
import api from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";

export default function AddAuthor() {
    const [name, setName] = useState("");
    const [bio, setBio] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post("/api/authors", { name, bio });
            alert("Yazar başarıyla eklendi!");
            // Yazar eklendikten sonra kitap ekleme sayfasına gidelim
            navigate("/add-book");
        } catch (err) {
            alert("Hata: " + err.message);
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: "500px" }}>
            <div className="card shadow p-4">
                <h2 className="mb-3">✍️ Yeni Yazar Ekle</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label">Yazar Adı</label>
                        <input className="form-control" value={name} onChange={e => setName(e.target.value)} required />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Biyografi</label>
                        <textarea className="form-control" rows="3" value={bio} onChange={e => setBio(e.target.value)} />
                    </div>
                    <button className="btn btn-primary w-100">Kaydet ve Devam Et</button>
                </form>
            </div>
        </div>
    );
}