import { useEffect, useState, useContext } from "react";
import api from "../api/axiosConfig";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Books() {
    const [books, setBooks] = useState([]);
    const [error, setError] = useState("");

    // Modal için gerekli State'ler
    const [showModal, setShowModal] = useState(false);
    const [myShelves, setMyShelves] = useState([]);
    const [selectedBookId, setSelectedBookId] = useState(null);

    const { user } = useContext(AuthContext);
    const navigate = useNavigate();

    // Sayfa açılınca kitapları çek
    useEffect(() => {
        fetchBooks();
    }, []);

    const fetchBooks = async () => {
        try {
            const response = await api.get("/api/books");
            setBooks(response.data);
        } catch (err) {
            setError("Kitaplar yüklenirken hata oluştu.");
            console.error(err);
        }
    };

    // 1. ADIM: Butona basınca rafları çek ve Modalı aç
    const handleOpenShelfModal = async (bookId) => {
        if (!user) {
            alert("Kitabı rafına eklemek için giriş yapmalısın.");
            navigate("/login");
            return;
        }

        try {
            const res = await api.get("/api/shelves");
            if (res.data.length === 0) {
                alert("Henüz hiç rafın yok! Önce 'Rafım' sayfasından bir raf oluştur.");
                navigate("/myshelf");
                return;
            }
            setMyShelves(res.data); // Rafları kaydet
            setSelectedBookId(bookId); // Hangi kitabın seçildiğini kaydet
            setShowModal(true); // Modalı aç
        } catch (err) {
            alert("Raflar yüklenirken hata oluştu.");
        }
    };

    // 2. ADIM: Modaldan bir raf seçilince kitabı oraya ekle
    const addToSpecificShelf = async (shelfId) => {
        try {
            await api.post(`/api/shelves/${shelfId}/items`, {
                bookId: selectedBookId,
                status: "PLANNED",
                progressPercent: 0
            });
            alert("Kitap seçilen rafa başarıyla eklendi! ✅");
            setShowModal(false); // Modalı kapat
        } catch (err) {
            alert("Bu kitap zaten o rafta olabilir.");
            setShowModal(false);
        }
    };

    const handleDelete = async (id) => {
        if(!window.confirm("Bu kitabı silmek istediğine emin misin?")) return;
        try {
            await api.delete(`/api/books/${id}`);
            setBooks(books.filter(b => b.id !== id));
        } catch (err) {
            alert("Silme işlemi başarısız.");
        }
    };

    return (
        <div className="container position-relative">
            <h2 className="mb-4">📖 Kütüphane Arşivi</h2>

            {user?.role === 'ROLE_ADMIN' && (
                <button onClick={() => navigate("/add-book")} className="btn btn-success mb-3">
                    + Yeni Kitap Ekle
                </button>
            )}

            {error && <div className="alert alert-danger">{error}</div>}

            <div className="row">
                {books.map((book) => (
                    <div key={book.id} className="col-md-3 mb-4">
                        <div className="card h-100 shadow-sm">
                            <img src={book.coverUrl || "https://via.placeholder.com/150"} className="card-img-top" alt={book.title} style={{ height: "200px", objectFit: "cover" }} />
                            <div className="card-body d-flex flex-column">
                                <h5 className="card-title">{book.title}</h5>
                                <p className="card-text text-muted small">{book.authorName}</p>
                                <p className="card-text text-truncate">{book.description}</p>
                                <div className="mt-auto">
                                    {/* GÜNCELLENEN KISIM: DETAY BUTONU ARTIK ÇALIŞIYOR */}
                                    <button
                                        onClick={() => navigate(`/books/${book.id}`)}
                                        className="btn btn-outline-primary btn-sm w-100 mb-2"
                                    >
                                        Detay
                                    </button>

                                    {/* USER İÇİN: Rafa Ekle Butonu */}
                                    {user && user.role !== 'ROLE_ADMIN' && (
                                        <button
                                            onClick={() => handleOpenShelfModal(book.id)}
                                            className="btn btn-warning btn-sm w-100 mb-2"
                                        >
                                            Rafa Ekle 📚
                                        </button>
                                    )}

                                    {user?.role === 'ROLE_ADMIN' && (
                                        <button onClick={() => handleDelete(book.id)} className="btn btn-danger btn-sm w-100">Sil 🗑️</button>
                                    )}
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {/* --- RAF SEÇME MODALI --- */}
            {showModal && (
                <div className="modal d-block" style={{ backgroundColor: "rgba(0,0,0,0.5)" }}>
                    <div className="modal-dialog modal-dialog-centered">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title">Hangi Rafa Eklensin?</h5>
                                <button type="button" className="btn-close" onClick={() => setShowModal(false)}></button>
                            </div>
                            <div className="modal-body">
                                <p>Lütfen kitabı eklemek istediğin rafı seç:</p>
                                <div className="d-grid gap-2">
                                    {myShelves.map(shelf => (
                                        <button
                                            key={shelf.id}
                                            onClick={() => addToSpecificShelf(shelf.id)}
                                            className="btn btn-outline-primary text-start"
                                        >
                                            📂 {shelf.name}
                                        </button>
                                    ))}
                                </div>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>İptal</button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}