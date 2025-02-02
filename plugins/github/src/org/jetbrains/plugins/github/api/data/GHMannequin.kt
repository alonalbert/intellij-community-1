// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.plugins.github.api.data

import org.jetbrains.plugins.github.api.data.pullrequest.GHPullRequestRequestedReviewer

class GHMannequin(
  id: String,
  override val login: String,
  override val url: String,
  override val avatarUrl: String,
  override val name: String?,
) : GHNode(id), GHActor, GHPullRequestRequestedReviewer {
  override val shortName: String = login
  override fun getPresentableName(): String = name ?: login
}
