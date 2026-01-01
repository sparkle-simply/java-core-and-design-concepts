// Product
class Computer {
    private String cpu;
    private String ram;
    private String storage;

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public void display() {
        System.out.println("Computer configuration: \n\ncpu: "+this.cpu+"\n\nram: "+this.ram+"\n\nstorage: "+this.storage);
    }
}

// Builder interface
interface Builder {
    void buildCPU();
    void buildRAM();
    void buildStorage();
    Computer getResult();
}

// Concrete Builder
class GamingComputerBuilder implements Builder {
    Computer computer = new Computer();

    void buildCPU() {
        computer.setCpu("Gaming CPU");
    }

    void buildRAM() {
        computer.setRam("16GB DDR4");
    }

    void buildStorage() {
        computer.setStorage("1TB SSD");
    }

    Computer getResult() {
        return computer;
    }
}

// Builder Director
class ComputerDirector {
    public void construct(Builder builder) {
        builder.buildCPU();
        builder.buildRAM();
        builder.buildStorage();
    }
}

// Client
public class Main {
    public static void main(String[] args) {
        GamingComputerBuilder gamingComputerBuilder = new GamingComputerBuilder();
        ComputerDirector computerDirector = new ComputerDirector();
        computerDirector.construct(gamingComputerBuilder);

        Computer gamingComputer = gamingComputerBuilder.getResult();
        gamingComputer.display();
    }
}