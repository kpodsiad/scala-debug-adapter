# scala-debug-adapter
[![build-badge][]][build]

[build]:       https://github.com/scalacenter/scala-debug-adapter/actions?query=branch%3Amain+workflow%3A%22Continuous+Integration%22
[build-badge]: https://github.com/scalacenter/scala-debug-adapter/workflows/Continuous%20Integration/badge.svg?branch=main

Implementation of the [Debug Adapter Protocol](https://microsoft.github.io/debug-adapter-protocol/) for Scala

# SBT Plugin

As a global plugin use `~/.sbt/1.0/plugins/plugins.sbt`, otherwise add to your local project (e.g. `project/plugins.sbt`):

```scala
addSbtPlugin("ch.epfl.scala" % "sbt-debug-adapter" % "1.0.0-SNAPSHOT")
```

# Project Goals

The library should be a depedency that [Bloop](https://github.com/scalacenter/bloop), [sbt](https://github.com/sbt/sbt), [Mill](https://github.com/lihaoyi/mill), or other [BSP](https://github.com/build-server-protocol/build-server-protocol) servers can import and use to implement [`debugSession/start`](https://github.com/build-server-protocol/build-server-protocol/blob/master/bsp4s/src/main/scala/ch/epfl/scala/bsp/endpoints/Endpoints.scala#L72) in BSP.

# Development

* [TestDebugClient](./test-client/src/main/scala/ch/epfl/scala/debugadapter/testing/TestDebugClient.scala) is a minimal debug client that is used to communicate with the real server via socket.
* [MainDebuggeeRunner](core/src/test/scala/ch/epfl/scala/debugadapter/MainDebuggeeRunner.scala) starts a real java process with debugging enabled so that the server can connect to it.

# References

- [Bloop Debuggig Referece](https://scalacenter.github.io/bloop/docs/debugging-reference)
- [Microsoft DAP for Java](https://github.com/microsoft/vscode-java-debug)
- [DebugAdapterProvider](https://github.com/build-server-protocol/build-server-protocol/issues/145)
- [Bloop's DAP](https://github.com/scalacenter/bloop/tree/master/frontend/src/main/scala/bloop/dap) &  [Bloop's DAP  Tests](https://github.com/scalacenter/bloop/tree/master/frontend/src/test/scala/bloop/dap)

# History

- [Origial project discussion](https://github.com/scalameta/metals-feature-requests/issues/168)
