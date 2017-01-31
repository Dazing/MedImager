package medview.medimager.foundation;

import java.awt.image.*;

import java.io.*;

public interface ImageDataObtainer
{
	public BufferedImage getFullImage( ) throws IOException;

	public BufferedImage getMediumImage( ) throws IOException;

	public BufferedImage getThumbImage( ) throws IOException;

	public byte[] getFullImageByteArray( ) throws IOException;

	public byte[] getMediumImageByteArray( ) throws IOException;

	public byte[] getThumbImageByteArray( ) throws IOException;
}
