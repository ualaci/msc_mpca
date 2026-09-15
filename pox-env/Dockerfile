FROM ubuntu:22.04

# Prevent interactive prompts during apt-get
ENV DEBIAN_FRONTEND=noninteractive

# Install Mininet, OpenVSwitch, Python, and networking tools
RUN apt-get update && apt-get install -y \
    mininet \
    openvswitch-switch \
    git \
    python3 \
    iproute2 \
    iputils-ping \
    net-tools \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Clone the POX repository (Python 3 compatible branch)
WORKDIR /opt
RUN git clone -b gar-experimental https://github.com/noxrepo/pox.git

WORKDIR /workspace