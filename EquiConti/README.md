# EquiConti (Android, Kotlin, Compose + Room)

Gestione **entrate/uscite** per clienti con cavalli in pensione.
- Anagrafica clienti (cognome, nome, telefono) e cavalli (quota mensile per cavallo).
- Movimenti con **saldo progressivo** calcolato a video.
- **Report** filtrabile per cliente e data (riporta *Saldo iniziale* calcolato fino alla data di inizio).
- **Addebito automatico** della quota mensile (somma delle quote dei cavalli) il **1° del mese** tramite WorkManager.
- Modifica e cancellazione (da implementare facilmente partendo dagli esempi).

## Come aprire e installare (APK)
1. Apri **Android Studio** → *Open* → seleziona la cartella `EquiConti`.
2. Consenti la sincronizzazione di Gradle (serve Internet).
3. Premi **Run** per installare su un telefono Android collegato via USB, oppure **Build > Build APK(s)** per generare l'APK.

> Nota: il progetto usa Kotlin, Jetpack Compose, Room, Hilt. Min SDK 24 (Android 7.0).

## Funzioni principali
- **Clienti**: lista, aggiunta, dettagli.
- **Cavalli**: aggiunta per cliente, quota mensile per cavallo.
- **Movimenti**: entrate/uscite con data e descrizione; saldo progressivo mostrato a destra.
- **Report**: per intervallo date, con riga iniziale *Saldo iniziale*.
- **Quota mensile**: il 1° di ogni mese viene creato un movimento in uscita pari alla somma delle quote dei cavalli del cliente.

## Da migliorare (se vuoi)
- Modifica/elimina movimenti e cavalli (aggiungi menu contestuale).
- Esportazione CSV/PDF.
- Backup/restore del DB.
- Multi-valuta o aliquote IVA.

## Struttura
- `data/` → Entity Room (`Owner`, `Horse`, `Txn`), DAO, Database, Repo.
- `ui/` → Schermate Compose.
- `vm/` → ViewModel con Hilt.
