package medview.medimager.foundation;

import java.io.*;

public interface AudioDataObtainer
{
	public abstract byte[] getAudioByteArray() throws IOException;
}
