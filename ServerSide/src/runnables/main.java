package runnables;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Handler;

public class main {

	Handler updateConversationHandler;
	
	public static void main(String[] args) {
		System.out.println(getLocalIpAddress());
		Thread serverThread = new Thread(new ServerThread());
		serverThread.start();
	}
	
	private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
                }
            }
        } catch (SocketException ex) {
            //Log.e("ServerActivity", ex.toString());
        }
        return null;
    }
	
}


