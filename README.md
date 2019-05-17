# Blockchain on WSN

### Scopo del progetto: Costruire una Blockchain per memorizzare le misurazioni effettuate dai mote

Una Blockchain è, letteralmente, una catena di Blocchi legati tra loro mediante l'hash di un blocco precedente. E' un sistema distribuito tra tutti gli utenti che partecipano alla creazione di nuovi blocchi.
Viste le notevoli limitazioni hardware dei sensori WSN si è deciso di porre alcune modifiche all'idea base della blockchain.

Il nostro sistema è attualmente centralizzato per minimizzare l'utilizzo di memoria richiesta sui Mote. Esisterà un dispositivo detto FullNode, rappresentato da un PC, che manterrà in memoria numerose variabili tra le quali l'intera blockchain.

La struttura iniziale della blockchain è stata oggigiorno sostituita dal DAG – Direct Acyclic Graph che corrisponde ad un grafo privo di cicli al suo interno e diretto, ovvero si può determinare un verso di “lettura” dei blocchi al suo interno. Altro sui DAG ai link:
 - Molto buono: <https://www.youtube.com/watch?v=CZxH1V_zoug> 
 - Molto meno buono: <https://www.cryptominando.it/2018/04/02/dag-blockchain/> 

### LightNode
I sensori che si occupano della costruzione dei blocchi sono detti LightNode. Questi eseguono le seguenti operazioni:
 - Richiedi al FullNode gli hash dei 2 blocchi precedenti (prevHash1, prevHash2)
 - Effettua delle misurazioni 
 - Calcola l'hash complessivo di: {moteID, Misurazioni,  prevHash1,  prevHash2, nonce}.
   - Il moteID rappresenta un ID univoco per ogni sensore WSN
   - Il nonce è un numero intero che provvede alla generazione di nuovi hash. Maggiori informazioni sui nonce si possono trovare su <https://en.bitcoin.it/wiki/Nonce> 
 - Cifra le informazioni di sopra in un Blocco e lo manda al FullNode
    #### ToDo (CIFRATURA) :
    Data la complessità nello gestire le chiavi private, pubbliche e conseguente cifratura dei messaggi, tale parte del progetto non è stata sviluppata. I LightNode dispongono di risorse minimali per quanto riguarda la sicurezza; sviluppare un sistema di cifratura leggero sia computazionalmente che temporalmente parlando richiede un attento studio.
    Nel paper [1] a pagina 5, si discute di un eventuale implementazione che risolve questa problematica, attraverso l'utilizzo di un sistema a chiave simmetrica che si inizializza mediante un ulteriore processo di distribuzione basato sul concetto di chiave pubblica e privata gestita dal FullNode come Certfication Authority.  

### FullNode
Il FullNode si occupa delle seguenti operazioni:
 - Generare due blocchi Genesi. Sono i 2 blocchi iniziali tramite i quali è possibile iniziare il DAG
 - Risponde al LightNode inviando gli hash dei 2 blocchi precedenti
   #### ToDo (Credit-Based PoW Mechanism):
   Nell'implementazione reale di un DAG, gli hash da inviare devono corrispondere ai blocchi meno utilizzati all'interno del DAG stesso.    Ciò equivale ad accrescere l'attendibilità dei nuovi blocchi, quelli inseriti più di recente. E' infatti possibile creare un blocco      con misurazioni (appositamente) inesatte e cercare di inserirlo nella struttura. Viene quindi richiesta la verifica di ciascun          blocco, sia nel momento precedente l'aggiunta al DAG, sia in un secondo momento. Quando (l'hash di) un blocco viene usato da            molteplici altri blocchi, la sua attendibilità aumenta poiché ritenuto sufficientemente affidabile da poter far parte del DAG.
 
 - Ricevuto il blocco di misurazioni dal LightNode, ne verifica la correttezza
   #### ToDo:
   Bisogna decifrare il blocco, determinare la correttezza dei dati in esso presenti, aggiornare la tabella di credibilità del LightNode    e, eventualmente, aggiungere il Blocco al DAG se tutti i controlli sono risultati soddisfacenti.

 - Aggiorna la difficoltà da superare per un determinato LightNode in base all'ultimo blocco che questi ha inviato al FullNode.
   #### ToDo:
   Aggiornare la difficoltà richiede grande conoscenza dei possibili attacchi alle blockchain, ai DAG, ai mote ed a molte altre            variabili presenti nel progetto in questione. Un'idea di come poter sviluppare quanto appena descritto la si può trovare a pagina 4,    capitolo "B. Credit-Based PoW Mechanism" nel paper [1]

Il sistema appena descritto, e' stato semi-implementato sul framework di TinyOS.
L'architettura, quindi, come gia' descritto precedemente sara' composta dal LightNode con un applicazione completamente riscritta e il FullNode funzionera' mediante una normale BaseStation che collegata al PC permettera' di ricevere i messaggi e elaborarli attraverso un applicazione scritta in Java definita come FullNode.
   
### Istruzioni sul LightNode :
  Per quanto riguarda, l'utilizzo dell'applicazione NesC relativo al LightNode all'interno della cartella stessa e' presente un file header dove e' possibile definire i seguenti parametri seguenti:
 - DELTA_TIP : intervallo di tempo per l'invio del nuovo tip, ad esso andranno inseriti eventualmente i tempi necessari per l'attuazione del PoW
 - DELTA_MEASURES : intervallo di tempo tra una rivelazione e l'altra delle temperature 

### Installazione FullNode :
   
### Riferimenti   
1 - “Towards Secure Industrial IoT: Blockchain System with Credit-Based Consensus Mechanism” a cura di Junqin Huang, Linghe Kong, Senior Member, IEEE, Guihai Chen, Min-You Wu, Xue Liu, Senior Member, IEEE, Peng Zeng.
