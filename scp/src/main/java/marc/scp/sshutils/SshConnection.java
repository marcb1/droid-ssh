package marc.scp.sshutils;

import com.jcraft.jsch.*;

import android.util.Log;

public class SshConnection
{
    // jsch objects
    private Channel             _channel;
    private JSch                _jsch;
    private Session             _session;

    private SessionUserInfo     _userInfo;

    private final String        _log = "SshConnection";
    protected CONNECTION_STATE  _state;

    protected enum CONNECTION_STATE
    {
        CONNECTED,
        CONNECTING,
        DISCONNECTED;
    }

    // SessionUserInfo not connected to UI at this point
    public SshConnection(SessionUserInfo user)
    {
        _jsch = new JSch();
        _channel = null;
        _userInfo = user;
        _state = CONNECTION_STATE.DISCONNECTED;

        try
        {
            _session = _jsch.getSession(_userInfo.getUser(), _userInfo.getHost(), _userInfo.getPort());
            _session.setHostKeyRepository(new FingerPrintRepository(_jsch));
            _session.setServerAliveInterval(10000);
        }
        catch(JSchException e)
        {
            Log.e(_log, "Exception caught while creating jsch _session", e);
            _session = null;
        }
        catch(Exception e)
        {
            Log.e(_log, "Exception caught while creating jsch _session", e);
            _session = null;
        }
    }

    private boolean setupSession()
    {
        boolean ret = false;
        if(_session == null)
        {
            return ret;
        }
        else if(_userInfo.usingRSA())
        {
            try
            {
                // load(alias, private, public, passphrase)
                KeyPair keyPair = KeyPair.load(_jsch, _userInfo.getRsa(), null);
                if (!keyPair.isEncrypted())
                {
                    _jsch.addIdentity(_userInfo.getHost(), keyPair.forSSHAgent(), null, null);
                }
                else
                {
                    String passphrase = _userInfo.promptInput("RSA Encrypted", "Please enter key passphrase");
                    keyPair.decrypt(passphrase);
                }
                ret = true;
            }
            catch(JSchException e)
            {
                _userInfo.handleException(e);
                Log.e(_log, "Exception caught while creating jsch session", e);
            }
        }
        _session.setUserInfo(_userInfo);
        return ret;
    }

    public boolean connect()
    {
        setupSession();
        return true;
    }

    public void disconnect()
    {
        if(_state != CONNECTION_STATE.DISCONNECTED)
        {
            if(_channel != null)
            {
                _channel.disconnect();
            }
            _session.disconnect();
            _state = CONNECTION_STATE.DISCONNECTED;
        }
    }

    //setters
    public void enableCompression(String level)
    {
        _session.setConfig("compression.s2c", "zlib@openssh.com,zlib,none");
        _session.setConfig("compression.c2s", "zlib@openssh.com,zlib,none");
        _session.setConfig("compression_level", level);
        Log.d(_log, "Compression enabled, level: " + level);
    }

    public void disableHostChecking()
    {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        Log.d(_log, "Host checking disabled");
        _session.setConfig(config);
    }

    //getters
    public boolean isConnected()
    {
        return _state != CONNECTION_STATE.DISCONNECTED;
    }

    public String getName()
    {
        return _userInfo.getHost();
    }

    public Channel getChannel()
    {
        return _channel;
    }

    //protected getters
    protected SessionUserInfo getUserInfo()
    {
        return _userInfo;
    }

    protected Session getSession()
    {
        return _session;
    }

    protected JSch getJsch()
    {
        return _jsch;
    }
}

