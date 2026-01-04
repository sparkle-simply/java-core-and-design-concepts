// Target interface
interface Printer {
    abstract void print();
}

// Adaptee
class LegacyPrinter {
    void printDocument() {
        System.out.println("Legacy Printer");
    }
}

// Adaptor
class PrinterAdaptor implements Printer {
    LegacyPrinter legacyPrinter;

    public PrinterAdaptor() {
        this.legacyPrinter = new LegacyPrinter();
    }

    @Override
    void print() {
        this.legacyPrinter.printDocument();
    }

}

// Client Code
class Client {
    void clientCode(Printer printer) {
        printer.print();
    }

    public static void main(String[] args) {
        PrinterAdaptor printerAdaptor = new PrinterAdaptor();
        clienrCode(printerAdaptor);
    }
}