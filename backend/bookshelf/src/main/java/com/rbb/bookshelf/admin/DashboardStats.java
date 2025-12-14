package com.rbb.bookshelf.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStats {
    private long totalUsers;
    private long totalBooks;
    private long totalShelves;
    private long totalRatings;
    private String topUser; // En çok yorum yapan kullanıcı
}