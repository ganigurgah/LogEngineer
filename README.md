# LogEngineer
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-blue.svg?style=for-the-badge)

Java tabanlı sistemler için geliştirilmiş, log verilerini mühendislik standartlarında işlemeyi ve anlamlandırmayı sağlayan bir araçtır.

## Temel Özellikler

* **Veri İşleme:** Ham log çıktılarını yapılandırılmış veri setlerine dönüştürür.
* **Maven Entegrasyonu:** Standart Maven yaşam döngüsüyle tam uyumlu çalışır.
* **Esnek Mimari:** Farklı Java projelerine kolayca entegre edilebilir ve genişletilebilir.
* **Performans:** Büyük log dosyalarını işlemek için optimize edilmiş bellek yönetimi sunar.

## Gereksinimler

* **Java:** JDK 11 veya üstü.
* **Build Tool:** Apache Maven.

## Kurulum

Projeyi yerel makinenize kurmak için aşağıdaki adımları izleyin:

```bash
# Repoyu klonlayın
git clone [https://github.com/ganigurgah/LogEngineer.git](https://github.com/ganigurgah/LogEngineer.git)

# Proje dizinine gidin
cd LogEngineer

# Bağımlılıkları yükleyin ve derleyin
mvn clean install
```
## Kullanım Örneği
Projenizi kendi Java uygulamanıza dahil ettikten sonra aşağıdaki gibi kullanabilirsiniz:

```Java

// Örnek başlangıç kodu
LogEngineer engineer = new LogEngineer();
engineer.start();
```
## Configuration
Apache ve JDK default logger için ```resources ``` altında configuration dosyaları bulunmaktadır.

## Geliştirme
Katkıda bulunmak için:

Bu depoyu fork edin.

Yeni bir özellik dalı (branch) oluşturun.

Değişikliklerinizi commit edin.

Pull Request açın.

## Lisans
Bu proje MIT lisansı kapsamında sunulmaktadır.

Geliştirici: [Gani Gürgah](https://linkedin.com/in/ganigurgah).
