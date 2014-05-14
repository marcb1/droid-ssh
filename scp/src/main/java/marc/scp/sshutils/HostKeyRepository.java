package marc.scp.sshutils;

/**
 * Created by Marc on 5/13/14.
 */

import android.content.Context;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.UserInfo;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class HostKeyRepository implements com.jcraft.jsch.HostKeyRepository.HostKeyRepository {
    public int check(String host, byte[] key) {
        return OK;
    }

    public void add(HostKey hostkey, UserInfo ui) {
    }

    public void remove(String host, String type) {
    }

    public void remove(String host, String type, byte[] key) {
    }

    public String getKnownHostsRepositoryID() {
        return null;
    }

    public HostKey[] getHostKey() {
        return null;
    }

    public HostKey[] getHostKey(String host, String type) {
        return null;
    }
}