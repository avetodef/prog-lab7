package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.concurrent.Callable;


public class RequestReader implements Callable<String> {

    private final InputStream socketInputStream;

    public RequestReader(InputStream socketInputStream) {
        this.socketInputStream = socketInputStream;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */

    @Override
     public String call(){
        try{
            String requestJson;
            StringBuilder builder = new StringBuilder();

            int byteRead;

            while ((byteRead = socketInputStream.read()) != -1) {
                if (byteRead == 0) break;
                builder.append((char) byteRead);
            }
            requestJson = builder.toString();

            return requestJson;
        }

        catch (SocketException e) {
            System.out.println("клиент лег поспать. жди.");
            while (true){}

        } catch (IOException e) {
            System.out.println("server razuchilsya chitat... wot pochemy: " + e.getMessage());

        } catch (NullPointerException e) {
            System.out.println("stalo pusto v dushe i v request'e: " + e.getMessage());
        }

        return null;
    }
}
