package com.tasnetwork.calibration.conveyor.plc;

/*import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.InputRegister;*/
import de.re.easymodbus.server.ModbusServer;
//import net.wimpi.modbus.ModbusCoupler;

public class JamodServer_delete_me {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*try {
            int port = 502;
            String ipAddress = "192.168.0.103";

            SimpleProcessImage spi = new SimpleProcessImage();
            
            // Create a valid InputRegister implementation
            spi.addInputRegister(new InputRegister() {
                private int value = 1234; // Example value

                @Override
                public int getValue() {
                    return value;
                }

                @Override
                public void setValue(int v) {
                    this.value = v;
                }


                
                @Override
                public byte[] toBytes() {
                    return new byte[]{(byte) (value >> 8), (byte) value}; // Convert to big-endian
                }

                @Override
                public short toShort() {
                    return (short) value; // Convert int to short
                }
                
                @Override
                public int toUnsignedShort() {
                    return value & 0xFFFF; // Convert to unsigned short (keep positive values)
                }
            });

            ModbusCoupler.getReference().setProcessImage(spi);
            ModbusTCPListener listener = new ModbusTCPListener(5);
            listener.start();
            System.out.println("✅ Jamod Server started on " + ipAddress + ":" + port);

        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }*/
		
		
	        ModbusServer server = new ModbusServer();
	        server.setPort(502);
	        
	        server.setClientConnectionTimeout(0);
	       //server.getServerSocket().setSoTimeout(60000);
	        // Increase timeout to avoid ReadTimeout issues
	        //server.setTimeout(0); // Keep connection open indefinitely
	        //server.setServerListening(true);
	        // Preload registers
	        server.holdingRegisters[0] = 650;
	        server.holdingRegisters[1] = 1500;
	        server.holdingRegisters[11] = 6888;

/*	        try {
	            System.out.println("🚀 Starting Modbus TCP Server on port 502...");
	            server.start();

	            while (true) {
	                Thread.sleep(1000); // Prevents high CPU usage
	            }
	        } catch (Exception e) {
	            System.out.println("❌ Error: " + e.getMessage());
	        } finally {
	            server.stop();
	            System.out.println("🛑 Server stopped.");
	        }*/
	        
	        Thread serverThread = new Thread(() -> {
	            try {
	                System.out.println("🚀 Starting Modbus TCP Server on port 502...");
	                server.Listen();
	            }catch (java.net.SocketTimeoutException e) {
                    System.out.println("⚠️ Timeout occurred, keeping connection open.");
                }  catch (Exception e) {
	                System.out.println("❌ Server Error: " + e.getMessage());
	            }
	        });

	        serverThread.start();

	        // Keep the main thread alive
	        while (true) {
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        
	    }
    }


