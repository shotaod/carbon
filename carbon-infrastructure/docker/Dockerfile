FROM debian:stretch-20171210

EXPOSE 22

RUN set -x && \
  echo '==================================================' && \
  echo 'install packages' && \
  echo '==================================================' && \
  apt update && \
  apt -y install sudo ssh python-dev && \
  apt clean

RUN set -x && \
  echo '==================================================' && \
  echo 'configure root user' && \
  echo '==================================================' && \
  echo "root:password" | chpasswd && \
  echo '==================================================' && \
  echo 'configure ssh' && \
  echo '==================================================' && \
  ssh-keygen -A && \
  sed -i s/#PermitRootLogin.*/PermitRootLogin\ yes/ /etc/ssh/sshd_config

COPY entrypoint.sh /usr/local/bin/
ENTRYPOINT ["entrypoint.sh"]
CMD ["/usr/sbin/sshd", "-D"]
