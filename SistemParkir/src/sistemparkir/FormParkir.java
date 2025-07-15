package sistemparkir; 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


public class FormParkir extends javax.swing.JFrame {
    private String oldPlat = null; //menyimpan plat nomor
    public FormParkir() {
        initComponents();
        loadData();
        initJenisKendaraan();
        setLocationRelativeTo(null);        

        txtPlatNomor.setText("");
        TglMasuk.setText(getWaktuSekarang());  //tanggal waktu otomatis      
        resetForm();
        tableParkir.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableParkir.getSelectedRow();
                if (row >= 0) {
                    txtPlatNomor.setText(tableParkir.getValueAt(row, 0).toString());
                    cmbJenisKendaraan.setSelectedItem(tableParkir.getValueAt(row, 1).toString());
                    TglMasuk.setText(tableParkir.getValueAt(row, 2).toString());
                }
            }
        });
        tableParkir.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableParkir.getSelectedRow();
                if (row >= 0) {
                    String selectedPlat = tableParkir.getValueAt(row, 0).toString(); //kalo mau reset ngisi ulang
                    txtPlatNomor.setText(selectedPlat);
                    cmbJenisKendaraan.setSelectedItem(tableParkir.getValueAt(row, 1).toString());
                    TglMasuk.setText(tableParkir.getValueAt(row, 2).toString());
                    oldPlat = selectedPlat; // ⬅️ SIMPAN YANG LAMA UNTUK WHERE
                }
            }
        });
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPlatNomor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbJenisKendaraan = new javax.swing.JComboBox<>();
        btnHapus = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnUbah = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableParkir = new javax.swing.JTable();
        btnKeluar = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        TglMasuk = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel1.setText("Formulir Kendaraan Masuk");

        jLabel2.setText("Nomor Plat");

        txtPlatNomor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlatNomorActionPerformed(evt);
            }
        });

        jLabel3.setText("Jenis Kendaraan");

        jLabel4.setText("Tanggal Masuk");

        cmbJenisKendaraan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbJenisKendaraanActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnSimpan.setText("Check In");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        tableParkir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Plat Nomor", "Jenis Kendaraan", "Tanggal Masuk", "Tanggal Keluar", "Durasi", "Total Biaya", "Denda"
            }
        ));
        jScrollPane1.setViewportView(tableParkir);

        btnKeluar.setText("Exit");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        TglMasuk.setText("Tanggal Masuk");

        jLabel12.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        jLabel12.setText("=====Daftar Kendaraan yang sedang parkir======");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(58, 58, 58)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbJenisKendaraan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtPlatNomor)
                                    .addComponent(TglMasuk, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)))
                            .addComponent(jLabel12)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnSimpan)
                                .addGap(29, 29, 29)
                                .addComponent(btnUbah)
                                .addGap(24, 24, 24)
                                .addComponent(btnHapus)
                                .addGap(18, 18, 18)
                                .addComponent(btnReset)
                                .addGap(18, 18, 18)
                                .addComponent(btnKeluar))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jLabel1)))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlatNomor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(TglMasuk)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(cmbJenisKendaraan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnUbah)
                    .addComponent(btnHapus)
                    .addComponent(btnReset)
                    .addComponent(btnKeluar))
                .addGap(64, 64, 64)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void initJenisKendaraan() {
        cmbJenisKendaraan.removeAllItems();
        cmbJenisKendaraan.addItem("Mobil");
        cmbJenisKendaraan.addItem("Motor");
        cmbJenisKendaraan.setSelectedIndex(-1);
    }

    // Method untuk ambil waktu sekarang
    private String getWaktuSekarang() { 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
    private boolean isPlatNomorSudahAda(String plat) {
        try {
            Connection conn = KoneksiDB.getConnection(); //ambil koneksi ke database dr kls KoneksiDB
            String sql = "SELECT COUNT(*) FROM parkir WHERE plat_nomor = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, plat);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal cek plat: " + e.getMessage());
        }
        return false;
    }
    private void loadData() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Plat Nomor", "Jenis Kendaraan", "Tanggal Masuk"}, 0);

        try {
            String sql = "SELECT * FROM parkir ORDER BY waktu_masuk DESC";
            Connection conn = KoneksiDB.getConnection(); // Koneksi database
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("plat_nomor"),
                    rs.getString("jenis"),
                    rs.getTimestamp("waktu_masuk").toString()
                });
            }

            tableParkir.setModel(model); // set model ke tabel setelah selesai isi data
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
        }
    }
    private void resetForm() {
        txtPlatNomor.setText("");
        cmbJenisKendaraan.setSelectedIndex(-1); 
        TglMasuk.setText(getWaktuSekarang()); 
    }
    private boolean isFormValid() {
        String plat = txtPlatNomor.getText().trim();
        String jenis = (cmbJenisKendaraan.getSelectedItem() != null) ? cmbJenisKendaraan.getSelectedItem().toString() : "";
        //masuk ke validasi
        if (plat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nomor plat belum diisi.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (jenis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jenis kendaraan belum dipilih.", "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void simpanData() {
        String plat = txtPlatNomor.getText().trim();
        String jenis = cmbJenisKendaraan.getSelectedItem().toString();
        String waktuMasuk = getWaktuSekarang();
        if (isPlatNomorSudahAda(plat)) {
            JOptionPane.showMessageDialog(this, "Plat nomor sudah terdaftar.");
            return;
        }
        try {
            String sql = "INSERT INTO parkir (plat_nomor, jenis, waktu_masuk) VALUES (?, ?, ?)";
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, plat);
            pst.setString(2, jenis);
            pst.setString(3, waktuMasuk);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data kendaraan berhasil disimpan.");
            resetForm();
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data: " + e.getMessage());
        }
    }
    private void ubahData() {
        String newPlat = txtPlatNomor.getText().trim();
        String jenis = cmbJenisKendaraan.getSelectedItem().toString();
        String waktuMasuk = TglMasuk.getText();

        if (oldPlat == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin diubah dari tabel.");
            return;
        }

        try {
            String sql = "UPDATE parkir SET plat_nomor = ?, jenis = ?, waktu_masuk = ? WHERE plat_nomor = ?";
            Connection conn = KoneksiDB.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, newPlat);         // plat baru
            pst.setString(2, jenis);
            pst.setString(3, waktuMasuk);
            pst.setString(4, oldPlat);         // plat lama untuk WHERE

            int updated = pst.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil diubah.");
                resetForm();
                loadData();
                oldPlat = null; // reset supaya tidak nyangkut
            } else {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau gagal diubah.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat mengubah data: " + e.getMessage());
        }
    }

    private void hapusData() {
        int selectedRow = tableParkir.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu.");
            return;
        }

        String plat = tableParkir.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data dengan plat nomor: " + plat + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM parkir WHERE plat_nomor = ?";
                Connection conn = KoneksiDB.getConnection();
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, plat);

                int deleted = pst.executeUpdate();

                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                    resetForm();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Data tidak ditemukan atau gagal dihapus.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error saat menghapus data: " + e.getMessage());
            }
        }
    }

 
    private void txtPlatNomorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlatNomorActionPerformed
        // TODO add your handling
    }//GEN-LAST:event_txtPlatNomorActionPerformed

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        if (isFormValid()) {
            ubahData();
        }
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        int konfirmasi = JOptionPane.showConfirmDialog(this, 
            "Yakin kembali ke menu utama?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            new SistemParkir().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void cmbJenisKendaraanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbJenisKendaraanActionPerformed

    }//GEN-LAST:event_cmbJenisKendaraanActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
    if (isFormValid()) {
            simpanData();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        resetForm();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        hapusData();
    }//GEN-LAST:event_btnHapusActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormParkir().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TglMasuk;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cmbJenisKendaraan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableParkir;
    private javax.swing.JTextField txtPlatNomor;
    // End of variables declaration//GEN-END:variables
}
