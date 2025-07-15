/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sistemparkir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.text.NumberFormat;
import java.util.Locale;


public class ParkirKeluar extends javax.swing.JFrame {

    private String platNomorAktif = null;
    private String jenisAktif = null;
    private String waktuMasukAktif = null;

    public ParkirKeluar() {
        initComponents();
        setDefaultFieldKeluar();
        loadDataParkirAktif();
        setLocationRelativeTo(null);
        platNomor.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String input = platNomor.getText();
                cariDataPlatNomor(input);
            }
        });
    }
    private void setDefaultFieldKeluar() {
        jenisKendaraan.setText("-");
        waktuMasuk.setText("-");
        waktuKeluar.setText("-");
        durasi.setText("-");
        totalTagihan.setText("-");
    }
    private void loadDataParkirAktif() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Plat Nomor");
        model.addColumn("Jenis Kendaraan");
        model.addColumn("Waktu Masuk");

        try {
            int no = 1;
            Connection conn = KoneksiDB.getConnection();
            String sql = "SELECT * FROM parkir ORDER BY waktu_masuk DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getString("plat_nomor"),
                    rs.getString("jenis"),
                    rs.getString("waktu_masuk")
                });
            }

            tableParkirAktif.setModel(model);

            rs.close();
            st.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data parkir: " + e.getMessage());
        }
    }
    
    private void cariDataPlatNomor(String input) {
        if (input.isEmpty()) {
            jenisKendaraan.setText("-");
            waktuMasuk.setText("-");
            waktuKeluar.setText("-");
            durasi.setText("-");
            totalTagihan.setText("-");
            platNomorAktif = null;
            return;
        }

        try {
            Connection conn = KoneksiDB.getConnection();
            String sql = "SELECT * FROM parkir WHERE plat_nomor LIKE ? ORDER BY waktu_masuk DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, input + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jenisKendaraan.setText(rs.getString("jenis"));
                waktuMasuk.setText(rs.getString("waktu_masuk"));

                platNomorAktif = rs.getString("plat_nomor"); // disimpan bukan dari TextField
                jenisAktif = rs.getString("jenis");
                waktuMasukAktif = rs.getString("waktu_masuk");

                // waktu keluar auto
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                waktuKeluar.setText(sdf.format(new Date()));
                hitungDurasiParkir();
                hitungTotalTagihan();
            } else {
                jenisKendaraan.setText("-");
                waktuMasuk.setText("-");
                waktuKeluar.setText("-");
                durasi.setText("-");
                totalTagihan.setText("-");  
                platNomorAktif = null;
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data: " + e.getMessage());
        }
    }
    private void hitungDurasiParkir() {
        try {
            String masukStr = waktuMasuk.getText();
            String keluarStr = waktuKeluar.getText();

            if (masukStr.equals("-") || keluarStr.equals("-")) {
                durasi.setText("-");
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime masuk = LocalDateTime.parse(masukStr, formatter);
            LocalDateTime keluar = LocalDateTime.parse(keluarStr, formatter);

            Duration duration = Duration.between(masuk, keluar);
            long jam = duration.toHours();
            long menit = duration.toMinutes() % 60;

            // Tampilkan durasi
           durasi.setText(jam + " jam " + menit + " menit (Dibulatkan: " + getDurasiJam(masukStr, keluarStr) + " jam)");

        } catch (Exception e) {
            durasi.setText("Durasi error");
        }
    }
    private long getDurasiJam(String masukStr, String keluarStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime masuk = LocalDateTime.parse(masukStr, formatter);
        LocalDateTime keluar = LocalDateTime.parse(keluarStr, formatter);

        Duration duration = Duration.between(masuk, keluar);
        long totalMenit = duration.toMinutes();
        return (long) Math.ceil(totalMenit / 60.0);
    }
    
    private long getTarifDasar(String jenis) {
        if (jenis.equalsIgnoreCase("motor")) return 5000;
        if (jenis.equalsIgnoreCase("mobil")) return 10000;
        return 0;
    }
    private long getTarifPerJam(String jenis) {
        if (jenis.equalsIgnoreCase("motor")) return 2000;
        if (jenis.equalsIgnoreCase("mobil")) return 5000;
        return 0;
    }
    private long getDenda(String jenis) {
        if (jenis.equalsIgnoreCase("motor")) return 50000;
        if (jenis.equalsIgnoreCase("mobil")) return 100000;
        return 0;
    }
    private void hitungTotalTagihan() {
        try {
            String jenis = jenisKendaraan.getText();
            String masukStr = waktuMasuk.getText();
            String keluarStr = waktuKeluar.getText();

            if (jenis.equals("-") || masukStr.equals("-") || keluarStr.equals("-")) {
                totalTagihan.setText("-");
                return;
            }

            long durasiJam = getDurasiJam(masukStr, keluarStr);
            long tarifFlat = getTarifDasar(jenis);
            long tarifPerJam = getTarifPerJam(jenis);
            long denda = getDenda(jenis);

            long total = 0;

            if (durasiJam <= 2) {
                total = tarifFlat;
            } else {
                total = tarifFlat + ((durasiJam - 2) * tarifPerJam);
            }

            if (durasiJam > 24) {
                total += denda;
            }
           NumberFormat nf = NumberFormat.getInstance(new Locale("id", "ID"));
           totalTagihan.setText("Rp" + nf.format(total));
        } catch (Exception e) {
            totalTagihan.setText("Error");
        }
    }
    
    private void resetFormKeluar() {
        platNomor.setText("");
        jenisKendaraan.setText("-");
        waktuMasuk.setText("-");
        waktuKeluar.setText("-");
        durasi.setText("-");
        totalTagihan.setText("-");
        platNomor.requestFocus();

        // reset internal state
        platNomorAktif = null;
        jenisAktif = null;
        waktuMasukAktif = null;
    }

    private boolean validasiFormKeluar() {
        if (platNomor.getText().isEmpty() ||
            jenisKendaraan.getText().equals("-") ||
            waktuMasuk.getText().equals("-") ||
            waktuKeluar.getText().equals("-") ||
            totalTagihan.getText().equals("-")) {

            JOptionPane.showMessageDialog(this, "Data belum lengkap.");
            return false;
        }
        return true;
    }
    private void simpanKeRiwayat() throws SQLException {
        if (platNomorAktif == null) throw new SQLException("Plat nomor tidak valid!");

        Connection conn = KoneksiDB.getConnection();
        String sql = "INSERT INTO riwayat_parkir (plat_nomor, jenis, waktu_masuk, waktu_keluar, durasi_jam, total_tagihan) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, platNomorAktif);
        ps.setString(2, jenisAktif);
        ps.setString(3, waktuMasukAktif);
        ps.setString(4, waktuKeluar.getText());
        ps.setInt(5, (int) getDurasiJam(waktuMasukAktif, waktuKeluar.getText()));
        ps.setDouble(6, parseTagihanToDouble(totalTagihan.getText()));
        ps.executeUpdate();

        ps.close();
        conn.close();
    }
    private void hapusDariParkirAktif() throws SQLException {
        if (platNomorAktif == null) throw new SQLException("Plat nomor tidak valid untuk dihapus!");

        Connection conn = KoneksiDB.getConnection();
        String sql = "DELETE FROM parkir WHERE plat_nomor = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, platNomorAktif);
        ps.executeUpdate();
        ps.close();
        conn.close();
    }
    private double parseTagihanToDouble(String tagihan) {
        return Double.parseDouble(tagihan.replace("Rp", "").replace(".", "").trim());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        platNomor = new javax.swing.JTextField();
        jenisKendaraan = new javax.swing.JLabel();
        waktuMasuk = new javax.swing.JLabel();
        waktuKeluar = new javax.swing.JLabel();
        keluarParkir = new javax.swing.JButton();
        keluarAplikasi = new javax.swing.JButton();
        resetForm = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableParkirAktif = new javax.swing.JTable();
        durasi = new javax.swing.JLabel();
        totalTagihan = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel4.setText("Plat Nomor");

        jLabel5.setText("Jenis Kendaraan");

        jLabel6.setText("Waktu Masuk");

        jLabel7.setText("Waktu Keluar");

        jLabel8.setText("Durasi");

        jLabel9.setText("Total Tagihan");

        platNomor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                platNomorActionPerformed(evt);
            }
        });

        jenisKendaraan.setText("jLabel1");

        waktuMasuk.setText("jLabel2");

        waktuKeluar.setText("jLabel3");

        keluarParkir.setText("CheckOut");
        keluarParkir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keluarParkirActionPerformed(evt);
            }
        });

        keluarAplikasi.setText("Exit");
        keluarAplikasi.setActionCommand("Keluar Form");
        keluarAplikasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keluarAplikasiActionPerformed(evt);
            }
        });

        resetForm.setText("Reset Form");
        resetForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetFormActionPerformed(evt);
            }
        });

        tableParkirAktif.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Plat Nomor", "Jenis Kendaraan", "Tanggal Masuk", "Tanggal Keluar", "Durasi", "Total Biaya", "Denda"
            }
        ));
        jScrollPane1.setViewportView(tableParkirAktif);

        durasi.setText("jLabel3");

        totalTagihan.setText("jLabel3");

        jLabel13.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        jLabel13.setText("=====Daftar Kendaraan yang sedang parkir======");

        jLabel14.setFont(new java.awt.Font("Segoe UI Emoji", 1, 24)); // NOI18N
        jLabel14.setText("Formulir Kendaraan Keluar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jLabel14))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(jLabel5)
                                .addComponent(jLabel7)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel9))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(65, 65, 65)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(waktuKeluar)
                                                .addComponent(waktuMasuk)
                                                .addComponent(durasi)
                                                .addComponent(totalTagihan)
                                                .addComponent(jenisKendaraan)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(63, 63, 63)
                                            .addComponent(platNomor, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(keluarParkir)
                                    .addGap(99, 99, 99)
                                    .addComponent(resetForm)
                                    .addGap(107, 107, 107)
                                    .addComponent(keluarAplikasi))))))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(platNomor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jenisKendaraan))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(waktuMasuk))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(waktuKeluar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(durasi))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalTagihan)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(keluarParkir)
                    .addComponent(resetForm)
                    .addComponent(keluarAplikasi))
                .addGap(29, 29, 29)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void keluarParkirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keluarParkirActionPerformed
        if (!validasiFormKeluar()) return;

        try {
            simpanKeRiwayat();
            hapusDariParkirAktif();
            JOptionPane.showMessageDialog(this, "Kendaraan berhasil keluar & data disimpan.");
            resetFormKeluar();
            loadDataParkirAktif();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saat proses keluar: " + e.getMessage());
        }
    }//GEN-LAST:event_keluarParkirActionPerformed

    private void keluarAplikasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keluarAplikasiActionPerformed
        int konfirmasi = JOptionPane.showConfirmDialog(this, 
            "Yakin kembali ke menu utama?", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            new SistemParkir().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_keluarAplikasiActionPerformed

    private void resetFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetFormActionPerformed
        resetFormKeluar();
    }//GEN-LAST:event_resetFormActionPerformed

    private void platNomorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_platNomorActionPerformed
        if (!platNomor.getText().trim().isEmpty()) {
            cariDataPlatNomor(platNomor.getText().trim());
        }
    }//GEN-LAST:event_platNomorActionPerformed

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ParkirKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ParkirKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ParkirKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParkirKeluar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ParkirKeluar().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel durasi;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jenisKendaraan;
    private javax.swing.JButton keluarAplikasi;
    private javax.swing.JButton keluarParkir;
    private javax.swing.JTextField platNomor;
    private javax.swing.JButton resetForm;
    private javax.swing.JTable tableParkirAktif;
    private javax.swing.JLabel totalTagihan;
    private javax.swing.JLabel waktuKeluar;
    private javax.swing.JLabel waktuMasuk;
    // End of variables declaration//GEN-END:variables
}
