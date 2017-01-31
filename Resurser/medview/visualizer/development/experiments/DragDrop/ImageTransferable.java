package medview.visualizer.development.experiments.DragDrop;
/*
 * ImageTransferable.java
 *
 * Created on June 19, 2002, 11:09 AM
 */

/**
 *
 * @author  d97nix
 * @version
 */
import com.sun.image.codec.jpeg.*;
import java.awt.*;
import java.awt.image.*; // BufferedImage
import java.awt.datatransfer.*; // Transferable
import java.io.*; // IOException, buffers
import java.util.*; // Arrays

public class ImageTransferable implements Transferable, ImageObserver {
    
    
    private Image image;
    
    //public static final DataFlavor plainTextFlavor = DataFlavor.plainTextFlavor;
    //public static final DataFlavor localStringFlavor = DataFlavor.stringFlavor;
    
    public static final DataFlavor[] flavors = {
        JPEGFlavor.getInstance()
        //StringTransferable.plainTextFlavor,
        //StringTransferable.localStringFlavor
        
    };        
    
    private static final java.util.List flavorList = Arrays.asList( flavors );
    
    // Constructor
    public ImageTransferable(Image in_img) {
        image = in_img;
        if (image == null)
            System.out.println("ImageTransferable warning: got null image in constructor");
        else
            System.out.println("Constructor stored image ok");
    }
    
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }
    public boolean isDataFlavorSupported( DataFlavor flavor ) {
        return (flavorList.contains(flavor));
    }

    // Request the data
    public synchronized java.lang.Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        
      /*  if (flavor.equals(StringTransferable.plainTextFlavor)) {
            String charset = flavor.getParameter("charset").trim();
            if(charset.equalsIgnoreCase("unicode")) {
                System.out.println("returning unicode charset");
                // uppercase U in Unicode here!
                return new ByteArrayInputStream(this.string.getBytes("Unicode"));
            } else {
                System.out.println("returning latin-1 charset");
                return new ByteArrayInputStream(this.string.getBytes("iso8859-1"));
            }
        } else if (StringTransferable.localStringFlavor.equals(flavor)) {
            return this.string;
        } else */ if (flavor.equals(JPEGFlavor.getInstance())) {
            // Encode this image as JPEG
            // Get an encoder
            
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(ostream);
            

            
            /*
              try {
                  MediaTracker tracker = new MediaTracker(this);
                  tracker.addImage(img, 0);
                              tracker.waitForID(0);
              } catch (Exception e) {
                System.out.println("Mediatracker exception: " + e);
              } */
            
              if (image == null) 
                  System.out.println("image är null");
            
              int width = image.getWidth(this);
              int height = image.getHeight(this);
              BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
              Graphics2D biContext = bi.createGraphics();
              biContext.drawImage(image, 0, 0, null); // draw image to the bufferedimage
            
            
            // koda
            
            encoder.encode(bi);
            
            byte[] bytearray = ostream.toByteArray();            
            return new ByteArrayInputStream(bytearray);
            
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
    
    public boolean imageUpdate(java.awt.Image image, int flags, int x, int y, int width, int height) {
        if ((flags & ImageObserver.ALLBITS) == 0) // not set
            return true; // more data is needed
        else
            return false; // No more data is needed, we are ready
    }
    
}


