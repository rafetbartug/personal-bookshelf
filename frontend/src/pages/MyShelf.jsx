import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function MyShelf() {
    const [shelves, setShelves] = useState([]); // Raflar listesi
    const [selectedShelf, setSelectedShelf] = useState(null); // Seçili rafın ID'si
    const [shelfItems, setShelfItems] = useState([]); // Seçili rafın kitapları
    const [newShelfName, setNewShelfName] = useState(""); // Yeni raf ismi inputu

    // Sayfa açılınca kullanıcının raflarını çek
    useEffect(() => {
        fetchShelves();
    }, []);

    // Seçili raf değişince içindeki kitapları çek
    useEffect(() => {
        if (selectedShelf) {
            fetchShelfItems(selectedShelf);
        }
    }, [selectedShelf]);

    const fetchShelves = async () => {
        try {
            const res = await api.get("/api/shelves");
            setShelves(res.data);
            // Eğer raf varsa ve hiçbiri seçili değilse, ilkini seç
            if (res.data.length > 0 && !selectedShelf) {
                setSelectedShelf(res.data[0].id);
            }
        } catch (err) {
            console.error("Raflar yüklenemedi", err);
        }
    };

    const fetchShelfItems = async (shelfId) => {
        try {
            const res = await api.get(`/api/shelves/${shelfId}/items`);
            setShelfItems(res.data);
        } catch (err) {
            console.error("Kitaplar yüklenemedi", err);
        }
    };

    const handleCreateShelf = async (e) => {
        e.preventDefault();
        try {
            const res = await api.post("/api/shelves", { name: newShelfName, isPublic: true });
            setShelves([...shelves, res.data]); // Listeye ekle
            setNewShelfName(""); // Inputu temizle
            setSelectedShelf(res.data.id); // Yeni rafı seç
        } catch (err) {
            alert("Raf oluşturulamadı.");
        }
    };

    const handleDeleteItem = async (itemId) => {
        if (!window.confirm("Bu kitabı raftan çıkarmak istiyor musun?")) return;
        try {
            await api.delete(`/api/shelves/${selectedShelf}/items/${itemId}`);
            // Listeden çıkar
            setShelfItems(shelfItems.filter(item => item.id !== itemId));
        } catch (err) {
            alert("Silme başarısız.");
        }
    };

    return (
        <div className="container mt-4">
            <div className="row">
                {/* SOL TARAFA: RAF LİSTESİ VE EKLEME */}
                <div className="col-md-4 mb-4">
                    <div className="card shadow-sm">
                        <div className="card-header bg-primary text-white">
                            📂 Raflarım
                        </div>
                        <ul className="list-group list-group-flush">
                            {shelves.map(shelf => (
                                <li
                                    key={shelf.id}
                                    className={`list-group-item list-group-item-action d-flex justify-content-between align-items-center ${selectedShelf === shelf.id ? 'active' : ''}`}
                                    onClick={() => setSelectedShelf(shelf.id)}
                                    style={{ cursor: "pointer" }}
                                >
                                    {shelf.name}
                                    <span className="badge bg-light text-dark rounded-pill">➤</span>
                                </li>
                            ))}
                            {shelves.length === 0 && <li className="list-group-item text-muted">Henüz rafın yok.</li>}
                        </ul>
                        <div className="card-body border-top">
                            <form onSubmit={handleCreateShelf} className="d-flex gap-2">
                                <input
                                    type="text"
                                    className="form-control form-control-sm"
                                    placeholder="Yeni Raf İsmi..."
                                    value={newShelfName}
                                    onChange={(e) => setNewShelfName(e.target.value)}
                                    required
                                />
                                <button className="btn btn-sm btn-success">+</button>
                            </form>
                        </div>
                    </div>
                </div>

                {/* SAĞ TARAF: SEÇİLİ RAFIN İÇERİĞİ */}
                <div className="col-md-8">
                    {selectedShelf ? (
                        <>
                            <h3 className="mb-3">📚 Raf İçeriği</h3>
                            {shelfItems.length === 0 ? (
                                <div className="alert alert-info">Bu rafta henüz kitap yok. "Tüm Kitaplar" menüsünden ekleyebilirsin.</div>
                            ) : (
                                <div className="row">
                                    {shelfItems.map(item => (
                                        <div key={item.id} className="col-md-6 mb-3">
                                            <div className="card h-100">
                                                <div className="card-body">
                                                    <h5 className="card-title">{item.bookTitle}</h5>
                                                    <h6 className="card-subtitle mb-2 text-muted">{item.authorName}</h6>
                                                    <div className="d-flex justify-content-between align-items-center mt-3">
                                                        <span className={`badge ${item.status === 'FINISHED' ? 'bg-success' : item.status === 'READING' ? 'bg-warning' : 'bg-secondary'}`}>
                                                            {item.status}
                                                        </span>
                                                        <small className="text-muted">% {item.progressPercent}</small>
                                                    </div>
                                                    <button
                                                        onClick={() => handleDeleteItem(item.id)}
                                                        className="btn btn-outline-danger btn-sm w-100 mt-3"
                                                    >
                                                        Raftan Çıkar
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            )}
                        </>
                    ) : (
                        <div className="alert alert-warning">
                            Lütfen soldan bir raf seç veya yeni bir tane oluştur.
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}