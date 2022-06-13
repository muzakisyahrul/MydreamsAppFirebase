package mobile.muzaki.mydreamsappfirebase

class Keinginan {
    var key: String? = null
    var nama: String? = null
    var deskripsi: String? = null
    var harga: Double? = null
    var terpenuhi : Double?=0.0
    var alamat: String? = ""
    var jenis_kelamin: String? = ""
    constructor() {}

    constructor(nama: String?, deskripsi: String?,harga: Double?,terpenuhi: Double?,alamat: String?,jenis_kelamin: String?) {
        this.nama = nama
        this.deskripsi = deskripsi
        this.harga = harga
        this.terpenuhi = terpenuhi
        this.alamat = alamat
        this.jenis_kelamin = jenis_kelamin
    }
}