import { useState, useEffect } from "react";
import api from "../api/axiosConfig";
import { useNavigate, Link } from "react-router-dom";

export default function AddBook() {
    const [authors, setAuthors] = useState([]);
    const [form, setForm] = useState({
        title: "", isbn: "", publishedYear: 2024, coverUrl: "", description: "", authorId: ""
    });

    const navigate = useNavigate();

    // Sayfa açılınca Yazarları çek (Dropdown için)
    useEffect(() => {
        api.get("/api/authors").then(res => setAuthors(res.data));
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post("/api/books", form);
            alert("Kitap başarıyla oluşturuldu!");
            navigate("/books"); // Listeye dön
        } catch (err) {
            alert("Hata oluştu: " + (err.response?.data?.message || err.message));
        }
    };

    return (
        <div className="container mt-5" style={{ maxWidth: "600px" }}>
            <div className="card shadow p-4">
                <h2 className="mb-4">📘 Yeni Kitap Ekle</h2>

                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label className="form-label">Kitap Başlığı</label>
                        <input className="form-control" required
                               onChange={e => setForm({...form, title: e.target.value})}
                        />
                    </div>

                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label className="form-label">ISBN</label>
                            <input className="form-control"
                                   onChange={e => setForm({...form, isbn: e.target.value})}
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label className="form-label">Yıl</label>
                            <input type="number" className="form-control" defaultValue={2024}
                                   onChange={e => setForm({...form, publishedYear: e.target.value})}
                            />
                        </div>
                    </div>

                    <div className="mb-3">
                        <label className="form-label">Yazar Seç</label>
                        <div className="d-flex gap-2">
                            <select className="form-select" required
                                    onChange={e => setForm({...form, authorId: e.target.value})}
                                    defaultValue=""
                            >
                                <option value="" disabled>Yazar Seçiniz...</option>
                                {authors.map(a => (
                                    <option key={a.id} value={a.id}>{a.name}</option>
                                ))}
                            </select>
                            <Link to="/add-author" className="btn btn-outline-secondary">+</Link>
                        </div>
                    </div>

                    <div className="mb-3">
                        <label className="form-label">Kapak Resmi URL</label>
                        <input type="url" className="form-control" placeholder="https://..."
                               onChange={e => setForm({...form, coverUrl: e.target.value})}
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label">Açıklama</label>
                        <textarea className="form-control" rows="3"
                                  onChange={e => setForm({...form, description: e.target.value})}
                        />
                    </div>

                    <button className="btn btn-success w-100">Kitabı Kaydet</button>
                </form>
            </div>
        </div>
    );
}