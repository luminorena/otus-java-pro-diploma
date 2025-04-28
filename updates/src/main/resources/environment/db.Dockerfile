FROM postgres:15.0

RUN sed -i '/ru_RU.UTF-8/s/^# //g' /etc/locale.gen
RUN locale-gen ru_RU.UTF-8
RUN localedef -i ru_RU -f UTF-8 -A /usr/share/locale/locale.alias ru_RU.UTF-8

ENV LANG=ru_RU.UTF-8
ENV LC_ALL=ru_RU.UTF-8