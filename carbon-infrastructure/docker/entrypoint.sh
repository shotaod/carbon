#!/bin/bash

if [ -z "$SSH_USER" ]; then
  echo '=================================================='
  echo 'No ssh user specified'
  echo '=================================================='
  exit 0
fi

if [ -e "/home/$SSH_USER" ]; then
  echo '=================================================='
  echo 'Skip step 1 & 2. (Already ssh user added)'
  echo '=================================================='
else
# 1 add user
  echo '=================================================='
  echo "1st. Add user @$SSH_USER to root"
  echo '=================================================='
  adduser --disabled-password --gecos "" --shell /bin/bash $SSH_USER
  adduser $SSH_USER root
  echo "$SSH_USER:$SSH_USER" | chpasswd
  echo "$SSH_USER ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

# 2 ssh setting
  echo '=================================================='
  echo "2nd. Configure ssh @$SSH_USER"
  echo '=================================================='
  CMD='cd;'
  CMD=$CMD'mkdir -m 700 .ssh;'
  CMD=$CMD"ssh-keygen -t ed25519 -N '' -f .ssh/id_$SSH_USER.key;"
  CMD=$CMD"mv '.ssh/id_$SSH_USER.key.pub'  .ssh/authorized_keys;"
  CMD=$CMD'chmod 600 .ssh/authorized_keys;'
  CMD=$CMD'echo "------------------------------";'
  CMD=$CMD"echo 'Here ssh secret key @$SSH_USER';"
  CMD=$CMD'echo "------------------------------";'
  CMD=$CMD"cat .ssh/id_$SSH_USER.key;"
  su -l "$SSH_USER" -c "$CMD"
fi
# 3 run sshd
echo '=================================================='
echo "3rd. Execution Command -> [$@]"
echo '=================================================='
# https://bugs.launchpad.net/ubuntu/+source/openssh/+bug/45234
if [ ! -e /var/run/sshd ]; then
  mkdir -m 755  /var/run/sshd
fi
exec "$@"
