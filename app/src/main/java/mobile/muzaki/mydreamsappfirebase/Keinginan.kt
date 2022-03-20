package mobile.muzaki.mydreamsappfirebase

class Keinginan {
    var key: String? = null
    var nama: String? = null
    var deskripsi: String? = null
    var harga: Double? = null
    var terpenuhi : Double?=0.0

    constructor() {}

    constructor(nama: String?, deskripsi: String?,harga: Double?,terpenuhi: Double?) {
        this.nama = nama
        this.deskripsi = deskripsi
        this.harga = harga
        this.terpenuhi = terpenuhi
    }
}