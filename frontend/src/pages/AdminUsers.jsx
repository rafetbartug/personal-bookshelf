import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

export default function AdminUsers() {
    const [users, setUsers] = useState([]);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const res = await api.get("/api/admin/users");
            setUsers(res.data);
        } catch (err) {
            alert("Kullanıcılar çekilemedi.");
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Bu kullanıcıyı silmek istediğine emin misin?")) return;
        try {
            await api.delete(`/api/admin/users/${id}`);
            setUsers(users.filter(u => u.id !== id));
        } catch (err) {
            alert("Silinemedi.");
        }
    };

    return (
        <div className="container mt-4">
            <h2 className="mb-4">👥 Kullanıcı Yönetimi</h2>
            <div className="card shadow-sm">
                <div className="card-body p-0">
                    <table className="table table-hover table-striped mb-0">
                        <thead className="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Kullanıcı Adı</th>
                            <th>E-Posta</th>
                            <th>Rol</th>
                            <th>İşlem</th>
                        </tr>
                        </thead>
                        <tbody>
                        {users.map(u => (
                            <tr key={u.id}>
                                <td>{u.id}</td>
                                <td className="fw-bold">{u.username}</td>
                                <td>{u.email || "-"}</td>
                                <td>
                                        <span className={`badge ${u.role === 'ROLE_ADMIN' ? 'bg-danger' : 'bg-primary'}`}>
                                            {u.role}
                                        </span>
                                </td>
                                <td>
                                    <button onClick={() => handleDelete(u.id)} className="btn btn-danger btn-sm">Sil 🗑️</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}