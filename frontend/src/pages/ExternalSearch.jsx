import { useState } from "react";
import api from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";

export default function ExternalSearch() {
    const [query, setQuery] = useState("");
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSearch = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            const res = await api.get(`/api/books/search-external?query=${query}`);
            setResults(res.data);
        } catch (err) {
            alert("Arama sırasında hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    const handleSaveToDb = async (book) => {
        try {
            await api.post("/api/books", {
                title: book.title,
                isbn: book.isbn || "000000",
                publishedYear: book.publishedYear,
                coverUrl: book.coverUrl,
                description: book.description,

                authorName: book.authorName,
                authorId: null
            });

            alert(`"${book.title}" ve yazarı başarıyla kaydedildi! ✅`);
            navigate("/books");
        } catch (err) {
            console.error(err);
            alert("Kaydetme başarısız: " + (err.response?.data?.message || "Bilinmeyen hata"));
        }
    };

    return (
        <div className="container mt-4">
            <h2 className="mb-4">🌍 OpenLibrary Kitap Ara</h2>

            <form onSubmit={handleSearch} className="d-flex gap-2 mb-4">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Kitap adı giriniz (Örn: Lord of the Rings)..."
                    value={query}
                    onChange={(e) => setQuery(e.target.value)}
                    required
                />
                <button className="btn btn-primary" disabled={loading}>
                    {loading ? "Aranıyor..." : "Ara 🔍"}
                </button>
            </form>

            <div className="row">
                {results.map((book, index) => (
                    <div key={index} className="col-md-3 mb-4">
                        <div className="card h-100 shadow-sm">
                            <img src={book.coverUrl || "https://via.placeholder.com/150"} className="card-img-top" style={{ height: "200px", objectFit: "cover" }} alt="cover" />
                            <div className="card-body d-flex flex-column">
                                <h6 className="card-title">{book.title}</h6>
                                <p className="small text-muted">{book.authorName}</p> {/* Yazar ismini burada da gösterelim */}
                                <p className="small text-muted">{book.publishedYear}</p>

                                <button
                                    onClick={() => handleSaveToDb(book)}
                                    className="btn btn-success btn-sm mt-auto w-100"
                                >
                                    Hızlı Kaydet ⚡
                                </button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}