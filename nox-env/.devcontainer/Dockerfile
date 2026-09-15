FROM ubuntu:14.04

# Ubuntu 14.04 is End-Of-Life, so we must point apt to the old-releases archive
RUN echo "deb http://old-releases.ubuntu.com/ubuntu/ trusty main restricted universe multiverse" > /etc/apt/sources.list && \
    echo "deb http://old-releases.ubuntu.com/ubuntu/ trusty-updates main restricted universe multiverse" >> /etc/apt/sources.list && \
    echo "deb http://old-releases.ubuntu.com/ubuntu/ trusty-security main restricted universe multiverse" >> /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -y --force-yes \
    autoconf \
    automake \
    g++ \
    libtool \
    python-dev \
    python-twisted \
    swig \
    libssl-dev \
    make \
    libboost-all-dev \
    git \
    wget \
    vim \
    && rm -rf /var/lib/apt/lists/*

# Clone the NOX repository
WORKDIR /opt
RUN git clone https://github.com/noxrepo/nox.git

# Build NOX from source
WORKDIR /opt/nox
RUN ./boot.sh && \
    mkdir build && \
    cd build && \
    ../configure && \
    make -j4

# Set up the workspace
WORKDIR /workspace
