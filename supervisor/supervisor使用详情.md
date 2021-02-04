### 参考于
[使用 supervisor 管理进程](https://liyangliang.me/posts/2015/06/using-supervisor/)  
[supervisor 安装、配置、常用命令](https://www.cnblogs.com/xueweihan/p/6195824.html)  
[supervisor常用命令](https://www.jianshu.com/p/f8735b039c67)

### [使用 supervisor 管理进程]
&emsp;&emsp;Supervisor (http://supervisord.org) 是一个用 Python 写的进程管理工具，可以很方便的用来启动、重启、关闭进程（不仅仅是 Python 进程）。除了对单个进程的控制，还可以同时启动、关闭多个进程，比如很不幸的服务器出问题导致所有应用程序都被杀死，此时可以用 supervisor 同时启动所有应用程序而不是一个一个地敲命令启动  

### 安装
&emsp;&emsp;Supervisor 可以运行在 Linux、Mac OS X 上。如前所述，supervisor 是 Python 编写的，所以安装起来也很方便，可以直接用 pip :  
`sudo pip install supervisor`  
&emsp;&emsp;pip 是一个 Python 的包管理工具，类似 java 的 maven，mac 自带 python 和 pip（ 或者 pip3，python 高版本支持)，[brew安装pip](https://www.jianshu.com/p/62bea7d3708c),[Ubuntu安装pip](https://linux.cn/article-10110-1.html)  

### supervisord 配置
&emsp;&emsp;Supervisor 相当强大，提供了很丰富的功能，不过我们可能只需要用到其中一小部分。安装完成之后，可以编写配置文件，来满足自己的需求。为了方便，我们把配置分成两部分：supervisord（supervisor 是一个 C/S 模型的程序，这是 server 端，对应的有 client 端：supervisorctl）和应用程序（即我们要管理的程序），管理程序supervisor的配置和被管理的应用程序的配置（供supervisor读入管理）  
&emsp;&emsp;首先来看 supervisord 的配置文件。安装完 supervisor 之后，可以运行echo_supervisord_conf 命令输出默认的配置项，也可以重定向到一个配置文件里：  
`echo_supervisord_conf > /etc/supervisord.conf`  
去除里面大部分注释和“不相关”的部分，我们可以先看这些配置：  
```
[unix_http_server]
file=/tmp/supervisor.sock   ; UNIX socket 文件，supervisorctl 会使用，和下面的配置要一致
;chmod=0700                 ; socket 文件的 mode，默认是 0700
;chown=nobody:nogroup       ; socket 文件的 owner，格式： uid:gid

;[inet_http_server]         ; HTTP 服务器，提供 web 管理界面
;port=127.0.0.1:9001        ; Web 管理后台运行的 IP 和端口，如果开放到公网，需要注意安全性
;username=user              ; 登录管理后台的用户名
;password=123               ; 登录管理后台的密码

[supervisord]
logfile=/tmp/supervisord.log ; 日志文件，默认是 $CWD/supervisord.log
logfile_maxbytes=50MB        ; 日志文件大小，超出会 rotate，默认 50MB
logfile_backups=10           ; 日志文件保留备份数量默认 10
loglevel=info                ; 日志级别，默认 info，其它: debug,warn,trace
pidfile=/tmp/supervisord.pid ; pid 文件
nodaemon=false               ; 是否在前台启动，默认是 false，即以 daemon 的方式启动
minfds=1024                  ; 可以打开的文件描述符的最小值，默认 1024
minprocs=200                 ; 可以打开的进程数的最小值，默认 200

; the below section must remain in the config file for RPC
; (supervisorctl/web interface) to work, additional interfaces may be
; added by defining them in separate rpcinterface: sections
[rpcinterface:supervisor]
supervisor.rpcinterface_factory = supervisor.rpcinterface:make_main_rpcinterface

[supervisorctl]
serverurl=unix:///tmp/supervisor.sock ; 通过 UNIX socket 连接 supervisord，路径与 unix_http_server 部分的 file 一致
;serverurl=http://127.0.0.1:9001 ; 通过 HTTP 的方式连接 supervisord

; 包含其他的配置文件
[include]
files = relative/directory/*.ini    ; 可以是 *.conf 或 *.ini
```
&emsp;&emsp;我们把上面这部分配置保存到 /etc/supervisord.conf（或其他任意有权限访问的文件），然后启动 supervisord（通过 -c 选项指定配置文件路径，如果不指定会按照这个顺序查找配置文件：$CWD/supervisord.conf, $CWD/etc/supervisord.conf, /etc/supervisord.conf）：
` supervisord -c /etc/supervisord.conf `   
查看 supervisord 是否在运行：  
` ps aux | grep supervisord `

### program 被管理的应用程序配置
&emsp;&emsp;上面我们已经把 supervisrod 运行起来了，现在可以添加我们要管理的进程的配置文件。这些配置可以都写到 supervisord.conf 文件里，如果应用程序很多，最好通过 include 的方式把不同的程序（组）写到不同的配置文件里。program 的配置文件就写在，supervisord 配置中 include 项的路径下：relative/directory/，然后 program 的配置文件命名规则推荐：app_name.conf，可以匹配    
```
[program:<服务名>]              ;其中 [program:服务名] 中的 服务名 是应用程序的唯一标识，不能重复。对该程序的所有操作（start, restart 等）都通过名字来实现
command=<启动命令>              ; 启动命令，与手动在命令行启动的命令是一样的
process_name=%(program_name)s ; process_name expr (default %(program_name)s)
numprocs=1                    ; number of processes copies to start (def 1)
directory=<运行目录>            ; directory to cwd to before exec (def no cwd)
;umask=022                     ; umask for process (default None)
;priority=999                  ; the relative start priority (default 999)
autostart=true                ; 在 supervisord 启动的时候也自动启动 start at supervisord start (default: true)
autorestart=unexpected        ; 程序异常退出后自动重启 whether/when to restart (default: unexpected)
startsecs=5                   ; 启动 5 秒后没有异常退出，就当作已经正常启动了    number of secs prog must stay running (def. 1)
startretries=3                ; 启动失败自动重试次数，默认是 3    max # of serial start failures (default 3)
exitcodes=0,2                 ; 'expected' exit codes for process (default 0,2)
stopsignal=QUIT               ; signal used to kill process (default TERM)
stopwaitsecs=10               ; max num secs to wait b4 SIGKILL (default 10)
stopasgroup=false             ; send stop signal to the UNIX process group (default false)
killasgroup=false             ; SIGKILL the UNIX process group (def false)
;user=skywell                  ; 用哪个用户启动 setuid to this UNIX account to run the program
;redirect_stderr=true          ; 把 stderr 重定向到 stdout，默认 false  redirect proc stderr to stdout (default false)
stdout_logfile=/var/log/<服务名>.log        ; stdout 日志文件大小，默认 50MB  stdout log path, NONE for none; default AUTO
stdout_logfile_maxbytes=1MB   ; max # logfile bytes b4 rotation (default 50MB)
stdout_logfile_backups=1     ; # of stdout logfile backups (default 10)
stdout_capture_maxbytes=1MB   ; number of bytes in 'capturemode' (default 0)
stdout_events_enabled=false   ; emit events on stdout writes (default false)
stderr_logfile=/var/log/<服务名>.err        ; stderr log path, NONE for none; default AUTO
stderr_logfile_maxbytes=1MB   ; max # logfile bytes b4 rotation (default 50MB)
stderr_logfile_backups=10     ; # of stderr logfile backups (default 10)
stderr_capture_maxbytes=1MB   ; number of bytes in 'capturemode' (default 0)
stderr_events_enabled=false   ; emit events on stderr writes (default false)
environment=A="1",B="2",HOME="/home/skywell"       ;  可以通过 environment 来添加需要的环境变量，一种常见的用法是使用指定的 virtualenv 环境 process environment additions (def no adds)
serverurl=AUTO                ; override serverurl computation (childutils)
```
### supervisorctl 操作
supervisorctl 是 supervisord 的命令行客户端工具，使用的配置和 supervisord 一样，这里就不再说了，
输入命令 supervisorctl 进入 supervisorctl 的 shell 交互界面（还是纯命令行😓），就可以在下面输入命令了。-c 指定配置文件路径（必须和服务端一致），否则与 supervisord 一样按照顺序查找配置文件  
` supervisorctl -c /etc/supervisord.conf `
>+ help # 查看帮助  
>+ status # 查看程序状态  
>+ stop program_name # 关闭 指定的程序  
>+ stop all 停止所有
>+ start program_name # 启动 指定的程序  
>+ restart program_name # 重启 指定的程序  
>+ tail -f program_name # 查看 该程序的日志  
>+ reread    ＃ 读取有更新（增加）的配置文件，不会启动新添加的程序  
>+ update # 重启配置文件修改过的程序（修改了配置，通过这个命令加载新的配置)  

上面这些命令都有相应的输出，除了进入 supervisorctl 的 shell 界面，也可以直接在 bash 终端运行:  
` supervisorctl -c /etc/supervisord.conf  stop userProgram`



















