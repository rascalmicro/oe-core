DESCRIPTION = "Command line tool and library for client-side URL transfers."
HOMEPAGE = "http://curl.haxx.se/"
BUGTRACKER = "http://curl.haxx.se/mail/list.cgi?list=curl-tracker"
SECTION = "console/network"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;beginline=7;md5=3a34942f4ae3fbf1a303160714e664ac"

DEPENDS = "zlib gnutls"
DEPENDS_virtclass-native = "zlib-native"
DEPENDS_virtclass-nativesdk = "zlib-nativesdk"
PR = "r0"

SRC_URI = "http://curl.haxx.se/download/curl-${PV}.tar.bz2 \
           file://noldlibpath.patch \
           file://pkgconfig_fix.patch"

SRC_URI[md5sum] = "0296d3196b4bf82c896a869b38dbc5f2"
SRC_URI[sha256sum] = "7d8c8cef8f244118245c37853cb175bd95c156ff453730ec2c88189b05a7c06e"

inherit autotools pkgconfig binconfig

EXTRA_OECONF = "--with-zlib=${STAGING_LIBDIR}/../ \
                --without-ssl \
                --without-libssh2 \
		--with-random=/dev/urandom \
		--without-libidn \
		--enable-crypto-auth \
                ${CURLGNUTLS} \
		"

CURLGNUTLS = " --with-gnutls=${STAGING_LIBDIR}/../"
CURLGNUTLS_virtclass-native = "--without-gnutls"
CURLGNUTLS_virtclass-nativesdk = "--without-gnutls"

do_configure_prepend() {
	sed -i s:OPT_GNUTLS/bin:OPT_GNUTLS:g configure.ac
}

PACKAGES += "${PN}-certs libcurl libcurl-dev libcurl-doc"

FILES_${PN} = "${bindir}/curl"

FILES_${PN}-certs = "${datadir}/curl/curl-*"
PACKAGE_ARCH_${PN}-certs = "all"

FILES_${PN}-doc = "${mandir}/man1/curl.1"

FILES_lib${BPN} = "${libdir}/lib*.so.*"
RRECOMMENDS_lib${BPN} += "${PN}-certs"
FILES_lib${BPN}-dev = "${includedir} \
                      ${libdir}/lib*.so \
                      ${libdir}/lib*.a \
                      ${libdir}/lib*.la \
                      ${libdir}/pkgconfig \
                      ${datadir}/aclocal \
                      ${bindir}/*-config"

FILES_lib${BPN}-doc = "${mandir}/man3 \
                      ${mandir}/man1/curl-config.1"

BBCLASSEXTEND = "native nativesdk"
