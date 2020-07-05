![](https://i.imgur.com/xeKD93p.png)
# `$ raspap-webgui` [![Release 1.6.2](https://img.shields.io/badge/Release-1.6.2-green.svg)](https://github.com/billz/raspap-webgui/releases) [![Awesome](https://awesome.re/badge.svg)](https://github.com/thibmaek/awesome-raspberry-pi)

A simple, responsive web interface to control wifi, hostapd and related services on the Raspberry Pi.

This project was inspired by a [**blog post**](http://sirlagz.net/2013/02/06/script-web-configuration-page-for-raspberry-pi/) by SirLagz about using a web page rather than ssh to configure wifi and hostapd settings on the Raspberry Pi. I began by prettifying the UI by wrapping it in [**SB Admin 2**](https://github.com/BlackrockDigital/startbootstrap-sb-admin-2), a Bootstrap based admin theme. Since then, the project has evolved to include greater control over many aspects of a networked RPi, better security, authentication, a Quick Installer, support for themes and more. RaspAP has been featured on sites such as [Instructables](http://www.instructables.com/id/Raspberry-Pi-As-Completely-Wireless-Router/), [Adafruit](https://blog.adafruit.com/2016/06/24/raspap-wifi-configuration-portal-piday-raspberrypi-raspberry_pi/), [Raspberry Pi Weekly](https://www.raspberrypi.org/weekly/commander/) and [Awesome Raspberry Pi](https://project-awesome.org/thibmaek/awesome-raspberry-pi) and implemented in countless projects.

We'd be curious to hear about how you use this with your own RPi-powered projects. Until then, here are some screenshots:

![](https://i.imgur.com/xyAdop0.gif)
![](https://i.imgur.com/T0vrSsA.gif)
![](https://i.imgur.com/n7Ir2cS.gif)
![](https://i.imgur.com/JdsNr9L.gif)
![](https://i.imgur.com/cYFnTIl.gif)
## Contents

 - [Prerequisites](#prerequisites)
 - [Quick installer](#quick-installer)
 - [Simultaneous AP and Wifi client](#simultaneous-ap-and-wifi-client)
 - [Support us](#support-us)
 - [Manual installation](#manual-installation)
 - [Multilingual support](#multilingual-support)
 - [HTTPS support](#https-support)
 - [Optional services](#optional-services)
 - [How to contribute](#how-to-contribute)
 - [Reporting issues](#reporting-issues)
 - [License](#license)

## Prerequisites
Start with a clean install of the [latest release of Raspbian](https://www.raspberrypi.org/downloads/raspbian/) (currently Buster). Raspbian Buster Lite is recommended.

1. Update Raspbian, including the kernel and firmware, followed by a reboot:
```
sudo apt-get update
sudo apt-get dist-upgrade
sudo reboot
```
2. Set the WiFi country in raspi-config's **Localisation Options**: `sudo raspi-config`

3. If you have an older Raspberry Pi without an onboard WiFi chipset, the [**Edimax Wireless 802.11b/g/n nano USB adapter**](https://www.edimax.com/edimax/merchandise/merchandise_detail/data/edimax/global/wireless_adapters_n150/ew-7811un) is an excellent option – it's small, cheap and has good driver support.

With the prerequisites done, you can proceed with either the Quick installer or Manual installation steps below.

## Quick installer
Install RaspAP from your RaspberryPi's shell prompt:
```sh
wget -q https://git.io/voEUQ -O /tmp/raspap && bash /tmp/raspap
```
The installer will complete the steps in the manual installation (below) for you.

After the reboot at the end of the installation the wireless network will be
configured as an access point as follows:
* IP address: 10.3.141.1
  * Username: admin
  * Password: secret
* DHCP range: 10.3.141.50 to 10.3.141.255
* SSID: `raspi-webgui`
* Password: ChangeMe

**Note:** As the name suggests, the Quick Installer is a great way to quickly setup a new AP. However, it does not automagically detect the unique configuration of your RPi. Best results are obtained by connecting an RPi to ethernet (`eth0`) or as a WiFi client, also known as managed mode, with `wlan0`. For the latter, refer to [this FAQ](https://github.com/billz/raspap-webgui/wiki/FAQs#how-do-i-prepare-the-sd-card-to-connect-to-wifi-in-headless-mode). Please [read this](https://github.com/billz/raspap-webgui/wiki/Reporting-issues) before reporting an issue.

## Simultaneous AP and Wifi client
RaspAP lets you easily create an AP with a Wifi client configuration. With your RPi configured in managed mode, enable the AP from the **Advanced** tab of **Configure hotspot** by sliding the **Wifi client AP mode** toggle. Save settings and start the hotspot. The managed mode AP is functional without restart.

![](https://i.imgur.com/4JkK2a5.png)

**Note:** For a Raspberry Pi operating in [managed mode](https://github.com/billz/raspap-webgui/wiki/FAQs#how-do-i-prepare-the-sd-card-to-connect-to-wifi-in-headless-mode) without an `eth0` connection, this configuration must be enabled _before_ a reboot. 

## Multilingual support
RaspAP uses [GNU Gettext](https://www.gnu.org/software/gettext/) to manage multilingual messages. In order to use RaspAP with one of our supported translations, you must configure a corresponding language package on your RPi. To list languages currently installed on your system, use `locale -a` at the shell prompt. To generate new locales, run `sudo dpkg-reconfigure locales` and select any other desired locales. Details are provided on our [wiki](https://github.com/billz/raspap-webgui/wiki/Translations#raspap-in-your-language). 

The following translations are currently maintained by the project:

- Deutsch
- Français
- Italiano
- Português
- Svenska
- Nederlands
- 简体中文 (Chinese Simplified)
- Čeština
- Русский
- Español
- Finnish
- Sinhala
- Türkçe

If your language is not in the list above, why not [contribute a translation](https://github.com/billz/raspap-webgui/wiki/Translations#contributing-a-translation)? Contributors will receive credit as the original translators.

## HTTPS support
A detailed how-to for enabling HTTPS is available [on our wiki](https://github.com/billz/raspap-webgui/wiki/HTTPS--support). 

## Optional services
OpenVPN and TOR are two additional services that run perfectly well on the RPi, and are a nice way to extend the usefulness of your WiFi router. I've started on interfaces to administer these services. Not everyone will need them, so for the moment they are disabled by default. You can enable them by changing these options in `/var/www/html/includes/config.php`:

```sh
// Optional services, set to true to enable.
define('RASPI_OPENVPN_ENABLED', false );
define('RASPI_TORPROXY_ENABLED', false );
```
Please note that these are only UI's for now. If there's enough interest I'll complete the funtionality for these optional admin screens.

## License
See the [LICENSE](./LICENSE) file.

