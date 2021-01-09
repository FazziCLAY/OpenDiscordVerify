# OpenDiscordVerify
Minecraft discord verify - Плагин для верефикации своей учётной записи discord через minecraft.

## plugin.yml:
```yml
discord:
  bot-token: ''
  channel: 7003271437920624252
  role: 700040569473564672
  mode: default
  change-nick-in-minecraft: true
  message-delete-delay: 7

code-generation:
  delay: 35
  minimum: 10000
  maximum: 99999

messages:
  code-command: '&eYou code &a%CODE%&e, use it in discord within 35 seconds.'
  code-expired: '&cCode expired!'
  code-you-already: '&eYou have already been given a code!'
  account-linked: '&aAccount linked! Account: &b%USER_NAME%#%USER_DISCRIMINATOR%&a!'
  discord-code-not-found: 'Code not found.'

```
### Вкладка discord:
**bot-token** - Токен discord бота.
**channel** - Id канала дискорд. Ниже будет пояснение.
**role** - Id роли которая будет выдана при успешной верефикации. 
**mode** - Если 'default' то в *channel* нужно будет написать полученый код командой /code. Если 'api' то в *channel* при команде /code будет отправлено сообщение с содержканием JSON компонента. Об этом ниже.
**change-nick-in-minecraft** - Если true, то при успешной верефикации ник в discord будет установлен на такой какой и в minecraft.
**message-delete-delay** - Через сколько секунд будет удалено сообщение бота об неуспешной верефикации в канале discord

### Вкладка code-generation:
123
