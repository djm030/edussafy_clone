export function normalizeText(value) {
  return String(value ?? '').trim().toLowerCase()
}

export function matchesText(item, fields, query) {
  const normalizedQuery = normalizeText(query)
  if (!normalizedQuery) return true

  return fields.some((field) => normalizeText(item[field]).includes(normalizedQuery))
}

export function filterByOption(items, option, query, optionFields, defaultFields) {
  const fields = optionFields[option] || defaultFields
  return items.filter((item) => matchesText(item, fields, query))
}

export function pageNumbers(items, pageSize) {
  const total = Math.max(1, Math.ceil(items.length / pageSize))
  return Array.from({ length: total }, (_, index) => index + 1)
}

export function paginateItems(items, activePage, pageSize) {
  const start = (activePage - 1) * pageSize
  return items.slice(start, start + pageSize)
}
